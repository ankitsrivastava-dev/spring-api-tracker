package com.ankitsrivastava.tracker;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

@Aspect
@Component
public class TrackerAspect {

    private final ApiTrackerProperties props;
    public final Deque<Map<String, Object>> logs = new ConcurrentLinkedDeque<>();

    public TrackerAspect(ApiTrackerProperties props) {
        this.props = props;
    }

    @Around("@annotation(com.ankitsrivastava.tracker.TrackApi)")
    public Object track(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!props.isEnabled()) {
            return joinPoint.proceed();
        }

        long start = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        TrackApi annotation = signature.getMethod().getAnnotation(TrackApi.class);
        String name = annotation.name();
        String methodName = signature.getDeclaringType().getSimpleName() + "." + signature.getMethod().getName();

        Map<String, Object> log = new HashMap<>();
        log.put("traceId", UUID.randomUUID().toString());
        log.put("method", methodName);
        log.put("displayName", name);
        log.put("timestamp", new Date());
        log.put("appName", props.getAppName());
        log.put("environment", props.getEnvironment());

        try {
            Object[] args = joinPoint.getArgs();
            List<Object> maskedParams = new ArrayList<>();
            for (Object arg : args) {
                if (arg != null && !isPrimitiveOrString(arg.getClass())) {
                    maskedParams.add(maskSensitiveFields(arg));
                } else {
                    maskedParams.add(arg);
                }
            }
            log.put("params", maskedParams);

            Object result = joinPoint.proceed();
            log.put("response", result);
            log.put("status", "SUCCESS");
            return result;
        } catch (Throwable t) {
            log.put("error", t.toString());
            log.put("status", "FAILED");
            throw t;
        } finally {
            log.put("executionTimeMs", System.currentTimeMillis() - start);
            logs.addFirst(log);
            while (logs.size() > 100) logs.removeLast();
            System.out.println("🔍 [Ankit Srivastava Tracker] " + log);
        }
    }

    private Object maskSensitiveFields(Object obj) {
        Map<String, Object> maskedMap = new HashMap<>();
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (field.isAnnotationPresent(Sensitive.class)) {
                    maskedMap.put(field.getName(), "**");
                } else {
                    maskedMap.put(field.getName(), field.get(obj));
                }
            } catch (IllegalAccessException ignored) {
                maskedMap.put(field.getName(), "ERROR");
            }
        }
        return maskedMap;
    }

    private boolean isPrimitiveOrString(Class<?> clazz) {
        return clazz.isPrimitive() ||
               clazz == String.class ||
               Number.class.isAssignableFrom(clazz) ||
               clazz == Boolean.class ||
               clazz == Character.class;
    }
}
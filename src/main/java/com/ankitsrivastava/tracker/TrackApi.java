package com.ankitsrivastava.tracker;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TrackApi {
    String name() default "";
}

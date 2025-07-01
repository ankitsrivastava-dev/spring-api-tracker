package com.ankitsrivastava.tracker;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "api-tracker")
public class ApiTrackerProperties {
    private boolean enabled = true;
    private boolean logEndpoint = true;
    private String appName = "unknown";
    private String environment = "default";

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public boolean isLogEndpoint() { return logEndpoint; }
    public void setLogEndpoint(boolean logEndpoint) { this.logEndpoint = logEndpoint; }

    public String getAppName() { return appName; }
    public void setAppName(String appName) { this.appName = appName; }

    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }
}

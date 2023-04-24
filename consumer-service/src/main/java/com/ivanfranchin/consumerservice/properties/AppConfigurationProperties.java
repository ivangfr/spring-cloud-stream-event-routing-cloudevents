package com.ivanfranchin.consumerservice.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfigurationProperties {

    private Map<String, String> routingMap;

    public Map<String, String> getRoutingMap() {
        return routingMap;
    }

    public void setRoutingMap(Map<String, String> routingMap) {
        this.routingMap = routingMap;
    }
}

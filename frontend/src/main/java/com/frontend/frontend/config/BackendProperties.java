package com.frontend.frontend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "backend.api")
public class BackendProperties {
    /**
     * Base URL del backend para consumir APIs JWT.
     */
    private String baseUrl = "http://localhost:8080";

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}

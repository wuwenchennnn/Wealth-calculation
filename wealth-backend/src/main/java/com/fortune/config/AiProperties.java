package com.fortune.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "ai")
public class AiProperties {
    private String provider;
    private String endpoint;
    private String apiKey;
    private String model;
    private Integer connectTimeoutMs;
    private Integer readTimeoutMs;
}

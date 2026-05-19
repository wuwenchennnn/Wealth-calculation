package com.fortune.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "audit")
public class AuditProperties {
    private Integer dailyLimit = 5;
    private Integer burstLimit = 20;
    private Integer cacheExpireMinutes = 1440;
}

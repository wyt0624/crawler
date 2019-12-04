package com.surfilter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "redis-info.key")
public class RedisKeyInfo {
    private String whileUrl;
    private String domainUrl;
    private String crawlerQueue;
    private String analiseQueue;
}

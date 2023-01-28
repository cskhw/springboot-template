package com.deliverylab.inspection.common;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix="app")
@Getter
@Setter
public class AppProperties {
    private String jwtSecret;

    private String jwtExpirationMs;

    private String jwtRefreshExpirationMs;
}

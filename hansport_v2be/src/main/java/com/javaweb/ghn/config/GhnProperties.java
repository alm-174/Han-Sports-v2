package com.javaweb.ghn.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ghn")
public record GhnProperties(
        String baseUrl,
        String token,
        Integer shopId,
        Integer fromDistrictId,
        String fromWardCode,
        String webhookToken,
        Integer defaultServiceId,
        Integer defaultLength,
        Integer defaultWidth,
        Integer defaultHeight,
        Integer defaultWeight
) {
}

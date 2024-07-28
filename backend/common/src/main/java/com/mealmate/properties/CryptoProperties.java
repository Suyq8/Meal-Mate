package com.mealmate.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "mealmate.security")
@Data
public class CryptoProperties {
    private String secretKey;
}

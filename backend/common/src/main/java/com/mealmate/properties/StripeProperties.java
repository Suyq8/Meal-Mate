package com.mealmate.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "mealmate.stripe")
@Data
public class StripeProperties {
    private String apiKey;
    private String signingSecret;
}

package com.mealmate.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "mealmate.jwt")
@Data
public class JwtProperties {

    /**
     * Configuration for generating JWT tokens for admin employees
     */
    private String adminSecretKey;
    private long adminTtl;
    private String adminTokenName;

    /**
     * Configuration for generating JWT tokens for WeChat users
     */
    private String userSecretKey;
    private long userTtl;
    private String userTokenName;

}

package com.mealmate.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "mealmate.gcp")
@Data
public class GCPProperties {
    private String bucketName;
    private String projectId;
    private String path;
}

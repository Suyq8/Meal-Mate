package com.mealmate.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mealmate.properties.GCPProperties;
import com.mealmate.utils.GCPUtil;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class GCPConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public GCPUtil gcpUtil(GCPProperties gcpProperties){
        log.info("init GCPUtil: {}", gcpProperties);
        return new GCPUtil(gcpProperties.getBucketName(), gcpProperties.getProjectId(), gcpProperties.getPath());
    }
}

package com.mealmate.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mealmate.properties.CryptoProperties;
import com.mealmate.utils.CryptoUtil;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class CryptoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public CryptoUtil cryptoUtil(CryptoProperties cryptoProperties){
        log.info("init CryptoUtil: {}", cryptoProperties);
        return new CryptoUtil(cryptoProperties.getSecretKey());
    }
}

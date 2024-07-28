package com.mealmate.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mealmate.properties.StripeProperties;
import com.mealmate.utils.StripeUtil;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class StripeConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public StripeUtil stripeUtil(StripeProperties stripeProperties){
        log.info("init stripeUtil: {}", stripeProperties);
        return new StripeUtil(stripeProperties.getApiKey(), stripeProperties.getSigningSecret());
    }
}

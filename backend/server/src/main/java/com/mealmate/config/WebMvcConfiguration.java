package com.mealmate.config;

import java.util.List;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.http.converter.HttpMessageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealmate.interceptor.JwtTokenAdminInterceptor;
import com.mealmate.json.JacksonObjectMapper;

/**
 * WebMvc Configuration
 */
@Configuration
@Slf4j
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    /**
     * Register custom interceptors
     *
     * @param registry the InterceptorRegistry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("Register custom interceptor");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/user/**")
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/employee/login");
    }

    /**
     * Configure custom message converters
     *
     * @param converters the list of HttpMessageConverters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new JacksonObjectMapper();
        converter.setObjectMapper(objectMapper);
        converters.add(4, converter);
    }

    /**
     * Configure custom OpenAPI documentation
     *
     * @return the custom OpenAPI object
     */
    @Bean
    public OpenAPI customOpenAPI() {
        log.info("Initialize OpenAPI documentation");
        return new OpenAPI()
                .info(new Info()
                        .title("mealmate API documentation")
                        .version("1.0")
                        .description("API documentation for mealmate project")
                        .license(new License().name("Apache 2.0")));
    }
}

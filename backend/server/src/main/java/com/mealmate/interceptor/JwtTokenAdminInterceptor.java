package com.mealmate.interceptor;

import com.mealmate.constant.JwtClaimsConstant;
import com.mealmate.properties.JwtProperties;
import com.mealmate.utils.JwtUtil;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.mealmate.context.BaseContext;

/**
 * Interceptor for validating JWT tokens
 */
@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * Validate JWT token
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Check if the intercepted request is a controller method or another resource
        if (!(handler instanceof HandlerMethod)) {
            // If it's not a dynamic method, allow the request to proceed
            return true;
        }

        // Get token from request cookie and validate it
        try {
            String token = request.getHeader("token");
            log.info("token: {}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
            BaseContext.setCurrentId(empId);
            log.info("employee id: {}", empId);

            return true;
        } catch (Exception ex) {
            // validation fails
            log.info("fail validation: {}", ex.getMessage());
            response.setStatus(401);
            return false;
        }
    }
}

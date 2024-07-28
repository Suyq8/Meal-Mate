package com.mealmate.aspect;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.mealmate.annotation.AutoFill;
import com.mealmate.constant.AutoFillConstant;
import com.mealmate.context.BaseContext;
import com.mealmate.enumeration.OperationType;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    
    @Pointcut("execution(* com.mealmate.mapper.*.*(..)) && @annotation(com.mealmate.annotation.AutoFill)")
    public void AutoFillPointCut() {
    }

    @Before("AutoFillPointCut()")
    public void AutoFill(JoinPoint joinPoint){
        log.info("begin autoFill: {}", joinPoint);

        // get operation type
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        AutoFill method = methodSignature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = method.value();

        Object[] args = joinPoint.getArgs();
        if (args.length==0){
            return;
        }

        Object entity = args[0];
        LocalDateTime now = LocalDateTime.now();
        Long userId = BaseContext.getCurrentId();

        switch (operationType){
            case OperationType.INSERT:
                try {
                    Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                    Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                    Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                    Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                    setCreateTime.invoke(entity, now);
                    setUpdateTime.invoke(entity, now);
                    setCreateUser.invoke(entity, userId);
                    setUpdateUser.invoke(entity, userId);
                } catch(Exception e){
                    log.error("insert autoFill error: {}", e.getMessage());
                }
            case OperationType.UPDATE:
                try {
                    Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                    Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                    setUpdateTime.invoke(entity, now);
                    setUpdateUser.invoke(entity, userId);
                } catch (Exception e){
                    log.error("update autoFill error: {}", e.getMessage());
                }
        }
    }
}

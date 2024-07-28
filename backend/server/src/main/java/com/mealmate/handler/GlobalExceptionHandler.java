package com.mealmate.handler;

import com.mealmate.exception.BaseException;
import com.mealmate.result.Result;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

import com.mealmate.constant.MessageConstant;

/**
 * Global exception handler to handle business exceptions thrown in the project.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * handle exception
     *
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result<String> exceptionHandler(BaseException ex) {
        log.error("Error: {}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * handle SQL exception
     *
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        String errorMessage = ex.getMessage();
        log.error("Error: {}", errorMessage);

        if (errorMessage.contains("Duplicate entry")) {
            int idxStart = errorMessage.indexOf("'") + 1;
            String userName = errorMessage.substring(idxStart, errorMessage.indexOf("'", idxStart));
            return Result.error(String.format(MessageConstant.USER_EXIST, userName));
        }

        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }
}

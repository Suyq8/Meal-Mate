package com.mealmate.exception;

public class UserExistException extends BaseException {
    public UserExistException() {
    }

    public UserExistException(String msg) {
        super(msg);
    }
}

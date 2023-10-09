package com.zerobase.reservation.exception.impl;

import com.zerobase.reservation.constants.CustomExceptionStatus;
import com.zerobase.reservation.exception.AbstractException;

public class UserNotFoundException extends AbstractException {
    @Override
    public int getStatusCode() {
        return CustomExceptionStatus.USER_NOT_FOUND.getCode();
    }

    @Override
    public String getMessage() {
        return CustomExceptionStatus.USER_NOT_FOUND.getMessage();
    }
}

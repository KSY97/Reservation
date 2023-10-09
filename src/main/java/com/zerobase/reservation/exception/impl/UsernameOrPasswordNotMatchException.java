package com.zerobase.reservation.exception.impl;

import com.zerobase.reservation.constants.CustomExceptionStatus;
import com.zerobase.reservation.exception.AbstractException;

public class UsernameOrPasswordNotMatchException extends AbstractException {
    @Override
    public int getStatusCode() {
        return CustomExceptionStatus.USERNAME_OR_PASSWORD_NOT_MATCH.getCode();
    }

    @Override
    public String getMessage() {
        return CustomExceptionStatus.USERNAME_OR_PASSWORD_NOT_MATCH.getMessage();
    }
}

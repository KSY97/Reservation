package com.zerobase.reservation.exception.impl;

import com.zerobase.reservation.constants.CustomExceptionStatus;
import com.zerobase.reservation.exception.AbstractException;

public class AccessDeniedException extends AbstractException {
    @Override
    public int getStatusCode() {
        return CustomExceptionStatus.ACCESS_DENIED.getCode();
    }

    @Override
    public String getMessage() {
        return CustomExceptionStatus.ACCESS_DENIED.getMessage();
    }
}

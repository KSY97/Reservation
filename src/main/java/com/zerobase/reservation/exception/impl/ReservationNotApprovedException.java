package com.zerobase.reservation.exception.impl;

import com.zerobase.reservation.constants.CustomExceptionStatus;
import com.zerobase.reservation.exception.AbstractException;

public class ReservationNotApprovedException extends AbstractException {
    @Override
    public int getStatusCode() {
        return CustomExceptionStatus.RESERVATION_NOT_APPROVED.getCode();
    }

    @Override
    public String getMessage() {
        return CustomExceptionStatus.RESERVATION_NOT_APPROVED.getMessage();
    }
}

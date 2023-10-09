package com.zerobase.reservation.exception.impl;

import com.zerobase.reservation.constants.CustomExceptionStatus;
import com.zerobase.reservation.exception.AbstractException;

public class AlreadyApprovedReservationException extends AbstractException {
    @Override
    public int getStatusCode() {
        return CustomExceptionStatus.ALREADY_APPROVED_RESERVATION.getCode();
    }

    @Override
    public String getMessage() {
        return CustomExceptionStatus.ALREADY_APPROVED_RESERVATION.getMessage();
    }
}

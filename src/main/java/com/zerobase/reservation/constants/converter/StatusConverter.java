package com.zerobase.reservation.constants.converter;

import com.zerobase.reservation.constants.ReservationStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class StatusConverter implements AttributeConverter<ReservationStatus, Integer> {
    // 예약 상태를 나타내기 위한 enum 형식의 ReservationStatus 를 db 컬럼에선 숫자코드로 나타내기 위한 컨버터

    @Override
    public Integer convertToDatabaseColumn(ReservationStatus status) {
        if(status == null) return null;
        return status.getCode();
    }

    @Override
    public ReservationStatus convertToEntityAttribute(Integer dbData) {
        if(dbData == null) return null;
        return ReservationStatus.ofCode(dbData);
    }
}

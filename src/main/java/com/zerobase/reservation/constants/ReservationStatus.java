package com.zerobase.reservation.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ReservationStatus {
    //예약 상태

    // 승인 전 상태
    UNAPPROVED("Unapproved", 0),
    // 예약 승인 상태
    APPROVED("Approved",  10),
    // 예약 거절 상태
    DENIED("Denied", 20),
    // 예약한 가게에 방문한 상태
    VISITED("Visited", 30);

    private String status;
    private Integer code;

    public static ReservationStatus ofCode(Integer dbData){
        return Arrays.stream(ReservationStatus.values())
                .filter(v -> v.getCode().equals(dbData))
                .findAny()
                .orElseThrow(() -> new RuntimeException("존재하지 않는 예약 상태 코드입니다."));
    }
}

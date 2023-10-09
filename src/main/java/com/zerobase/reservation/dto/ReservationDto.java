package com.zerobase.reservation.dto;

import com.zerobase.reservation.repository.entity.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDto {

    private Long reservationId;
    private String memberName;
    private String storeName;
    private String status;
    private LocalDateTime reservationDate;
    private LocalDateTime reservatedAt;

    public static ReservationDto fromEntity(Reservation reservation){
        return ReservationDto.builder()
                .reservationId(reservation.getId())
                .memberName(reservation.getMember().getUsername())
                .storeName(reservation.getStore().getName())
                .status(reservation.getStatus().getStatus())
                .reservationDate(reservation.getReservationDate())
                .reservatedAt(reservation.getReservatedAt())
                .build();
    }
}

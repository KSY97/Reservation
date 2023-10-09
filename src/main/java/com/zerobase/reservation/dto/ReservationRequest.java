package com.zerobase.reservation.dto;

import com.zerobase.reservation.constants.ReservationStatus;
import com.zerobase.reservation.repository.entity.Member;
import com.zerobase.reservation.repository.entity.Reservation;
import com.zerobase.reservation.repository.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ReservationRequest {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Reserve {

        private Long memberId;
        private Long storeId;
        private LocalDateTime reservationDate;

        public Reservation toEntity(Member member, Store store) {
            return Reservation.builder()
                    .reservationDate(this.reservationDate)
                    .member(member)
                    .store(store)
                    .status(ReservationStatus.UNAPPROVED)
                    .reservatedAt(LocalDateTime.now())
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangeStatus {
        private Long reservationId;
    }

}

package com.zerobase.reservation.repository.entity;

import com.zerobase.reservation.constants.ReservationStatus;
import com.zerobase.reservation.constants.converter.StatusConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Store store;

    @Convert(converter = StatusConverter.class)
    private ReservationStatus status;

    private LocalDateTime reservationDate;

    @CreatedDate
    private LocalDateTime reservatedAt;

    public void updateReservationStatus(ReservationStatus reservationStatus){
        this.status = reservationStatus;
    }
}

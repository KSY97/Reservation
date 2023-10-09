package com.zerobase.reservation.repository;

import com.zerobase.reservation.repository.entity.Reservation;
import com.zerobase.reservation.repository.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Review findByReservation(Reservation reservation);

    boolean existsByReservation(Reservation reservation);
}

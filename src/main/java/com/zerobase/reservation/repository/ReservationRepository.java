package com.zerobase.reservation.repository;

import com.zerobase.reservation.repository.entity.Member;
import com.zerobase.reservation.repository.entity.Reservation;
import com.zerobase.reservation.repository.entity.Store;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByMember(Member member, Pageable pageable);

    List<Reservation> findByMember(Member member);

    List<Reservation> findByStore(Store store);

    List<Reservation> findByStoreOrderByReservationDate(Store store, Pageable pageable);



}

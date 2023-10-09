package com.zerobase.reservation.repository;

import com.zerobase.reservation.repository.entity.Partner;
import com.zerobase.reservation.repository.entity.Store;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findByPartner(Partner partner);

    List<Store> findAllByOrderByName(Pageable pageable);

}

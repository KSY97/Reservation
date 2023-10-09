package com.zerobase.reservation.repository;

import com.zerobase.reservation.repository.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long>{

    Optional<Partner> findByUsername(String username);

    boolean existsByUsername(String username);
}

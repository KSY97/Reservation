package com.zerobase.reservation.service;

import com.zerobase.reservation.dto.StoreDto;
import com.zerobase.reservation.dto.StoreRequest;
import com.zerobase.reservation.exception.impl.AccessDeniedException;
import com.zerobase.reservation.exception.impl.UserNotFoundException;
import com.zerobase.reservation.repository.PartnerRepository;
import com.zerobase.reservation.repository.StoreRepository;
import com.zerobase.reservation.repository.entity.Partner;
import com.zerobase.reservation.repository.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final PartnerRepository partnerRepository;

    // 가게 등록
    @Transactional
    public StoreDto register(StoreRequest.Register storeRequest, String username) {
        // 요청에서 파트너 회원의 아이디를 찾아 유효하지 않으면 에러 발생
        Partner partner = partnerRepository.findById(storeRequest.getPartnerId())
                .orElseThrow(() -> new UserNotFoundException());

        // 인증키에 있는 아이디와 요청하려는 파트너 아이디가 다르면 에러 발생
        if(!username.equals(partner.getUsername())){
            throw new AccessDeniedException();
        }

        return StoreDto.fromEntity(storeRepository.save(storeRequest.toEntity(partner)));
    }

    // 파트너 아이디로 등록 된 가게 찾기
    public List<StoreDto> getStoresByPartnerId(Long partnerId){
        // 요청에서 파트너 회원의 아이디를 찾아 유효하지 않으면 에러 발생
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new UserNotFoundException());

        List<Store> stores = storeRepository.findByPartner(partner);

        return stores.stream()
                .map(StoreDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 모든 가게 찾기(가게 이름 순으로 정렬)
    public List<StoreDto> getStoresSortOrderByStoreName(int pageNo){
        Pageable pageable = PageRequest.of(pageNo, 10);
        List<Store> stores = storeRepository.findAllByOrderByName(pageable);

        return stores.stream()
                .map(StoreDto::fromEntity)
                .collect(Collectors.toList());
    }

}

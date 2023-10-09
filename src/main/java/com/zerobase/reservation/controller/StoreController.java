package com.zerobase.reservation.controller;

import com.zerobase.reservation.dto.StoreDto;
import com.zerobase.reservation.dto.StoreRequest;
import com.zerobase.reservation.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {
    // 파트너가 등록하는 스토어(가게)를 위한 컨트롤러

    private final StoreService storeService;

    // 가게 등록
    @PreAuthorize("hasRole('ROLE_PARTNER')")
    @PostMapping("/register")
    public ResponseEntity<?> registerStore(
            @RequestBody StoreRequest.Register request,
            Principal principal
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(storeService.register(request, principal.getName()));
    }

    // 파트너 id를 통해 등록한 가게 찾기
    @GetMapping
    public List<StoreDto> getStoresByPartnerId(
            @RequestParam("partner_id") Long partnerId
    ){
        return storeService.getStoresByPartnerId(partnerId);
    }

    // 모든 가게 찾기(가게 이름 순으로 정렬)
    @GetMapping("/search")
        public List<StoreDto> getSortedStoresByStoreName(
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo
    ){
        return storeService.getStoresSortOrderByStoreName(pageNo);
    }
}

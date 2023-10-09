package com.zerobase.reservation.controller;

import com.zerobase.reservation.dto.ReservationDto;
import com.zerobase.reservation.dto.ReservationRequest;
import com.zerobase.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {
    // 예약관련 동작을 위한 컨트롤러

    private final ReservationService reservationService;

    // 멤버가 가게에 예약
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping
    public ResponseEntity<?> reserveStore(
            @RequestBody ReservationRequest.Reserve request,
            Principal principal
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(reservationService.reservation(request, principal.getName()));
    }

    // memberId를 이용해 해당 멤버가 예약을 한 모든 예약들을 확인
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @GetMapping("/member")
    public List<ReservationDto> getReservationListByMemberId(
            @RequestParam("member_id") Long memberId,
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
            Principal principal
    ){
        return reservationService.getReservationByMemberId(memberId, pageNo, principal.getName());
    }

    // storeId를 이용해 해당 가게에 있는 모든 예약을 예약 날짜 오름차순으로 정렬해 확인
    @PreAuthorize("hasRole('ROLE_PARTNER')")
    @GetMapping("/store")
    public List<ReservationDto> getReservationByStoreId(
            @RequestParam("store_id") Long storeId,
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
            Principal principal
    ){
        return reservationService.getReservationByStoreIdOrderByReservationDate(storeId, pageNo, principal.getName());
    }

    // 예약 승인
    @PreAuthorize("hasRole('ROLE_PARTNER')")
    @PutMapping("/approve")
    public ResponseEntity<?> reservationApprove(
            @RequestBody ReservationRequest.ChangeStatus request,
            Principal principal
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(reservationService.reservationAccessApprove(request, principal.getName()));
    }

    // 예약 거절
    @PreAuthorize("hasRole('ROLE_PARTNER')")
    @PutMapping("/deny")
    public ResponseEntity<?> reservationDeny(
            @RequestBody ReservationRequest.ChangeStatus request,
            Principal principal
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(reservationService.reservationAccessDeny(request, principal.getName()));
    }

    // 예약 방문
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PutMapping("/visit")
    public ResponseEntity<?> reservationVisit(
            @RequestBody ReservationRequest.ChangeStatus request,
            Principal principal
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(reservationService.reservationVisit(request, principal.getName()));
    }

}

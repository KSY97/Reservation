package com.zerobase.reservation.service;

import com.zerobase.reservation.constants.ReservationStatus;
import com.zerobase.reservation.dto.ReservationDto;
import com.zerobase.reservation.dto.ReservationRequest;
import com.zerobase.reservation.exception.impl.*;
import com.zerobase.reservation.repository.MemberRepository;
import com.zerobase.reservation.repository.ReservationRepository;
import com.zerobase.reservation.repository.StoreRepository;
import com.zerobase.reservation.repository.entity.Member;
import com.zerobase.reservation.repository.entity.Reservation;
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
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    // 예약 생성
    @Transactional
    public ReservationDto reservation(ReservationRequest.Reserve reserveRequest, String username) {
        // 요청에서 멤버 회원의 아이디를 찾아 유효하지 않으면 에러 발생
        Member member = memberRepository.findById(reserveRequest.getMemberId())
                .orElseThrow(() -> new RuntimeException("멤버 회원을 찾을 수 없습니다."));

        // 인증키에 있는 아이디와 요청하려는 멤버 아이디가 다르면 에러 발생
        if(!username.equals(member.getUsername())){
            throw new RuntimeException("요청 권한이 없습니다.");
        }

        // 요청에서 스토어의 아이디를 찾아 유효하지 않으면 에러 발생
        Store store = storeRepository.findById(reserveRequest.getStoreId())
                .orElseThrow(() -> new RuntimeException("가게를 찾을 수 없습니다."));

        return ReservationDto.fromEntity(reservationRepository.save(reserveRequest.toEntity(member, store)));
    }

    // 멤버 id를 통한 예약 조회
    public List<ReservationDto> getReservationByMemberId(Long memberId, int pageNo, String username){
        // 요청에서 멤버 회원의 아이디를 찾아 유효하지 않으면 에러 발생
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("멤버 회원을 찾을 수 없습니다."));

        // 인증키에 있는 아이디와 요청하려는 멤버 아이디가 다르면 에러 발생
        if(!username.equals(member.getUsername())){
            throw new RuntimeException("요청 권한이 없습니다.");
        }

        Pageable pageable = PageRequest.of(pageNo, 10);

        // 찾은 멤버가 예약한 예약 건들을 리스트에 담는다
        List<Reservation> reservations = reservationRepository.findByMember(member, pageable);

        return reservations.stream()
                .map(ReservationDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 가게 id를 통한 예약 조회(예약 일자순으로 정렬)
    public List<ReservationDto> getReservationByStoreIdOrderByReservationDate(Long storeId, int pageNo, String username){
        // 요청에서 스토어의 아이디를 찾아 유효하지 않으면 에러 발생
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException());

        Pageable pageable = PageRequest.of(pageNo, 10);

        // 인증키에 있는 아이디와 요청하려는 스토어를 소유한 파트너의 아이디가 다르면 에러 발생
        if(!username.equals(store.getPartner().getUsername())){
            throw new RuntimeException("요청 권한이 없습니다.");
        }

        // 찾은 스토어에 걸려있는 예약 건들을 리스트에 담는다
        List<Reservation> reservations = reservationRepository.findByStoreOrderByReservationDate(store, pageable);

        return reservations.stream()
                .map(ReservationDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 예약 승인
    @Transactional
    public ReservationDto reservationAccessApprove(ReservationRequest.ChangeStatus changeRequest, String username){
        // 요청에서 예약 아이디를 찾아 유효하지 않으면 에러 발생
        Reservation reservation = reservationRepository.findById(changeRequest.getReservationId())
                .orElseThrow(() -> new ReservationNotFoundException());

        // 인증키에 있는 아이디와 요청하려는 예약의 스토어를 소유한 파트너의 아이디가 다르면 에러 발생
        if(!username.equals(reservation.getStore().getPartner().getUsername())){
            throw new RuntimeException("요청 권한이 없습니다.");
        }

        // 이미 방문한 예약의 상태 승인 불가능
        // 이미 승인한 예약의 상태 승인 불가능
        if(reservation.getStatus() == ReservationStatus.VISITED){
            throw new AlreadyVisitedReservationException();
        } else if(reservation.getStatus() == ReservationStatus.APPROVED){
            throw new AlreadyApprovedReservationException();
        }

        // 찾은 예약의 상태를 예약 승인 상태로 변경
        reservation.updateReservationStatus(ReservationStatus.APPROVED);

        return ReservationDto.fromEntity(reservationRepository.save(reservation));
    }

    // 예약 거절
    @Transactional
    public ReservationDto reservationAccessDeny(ReservationRequest.ChangeStatus changeRequest, String username){
        // 요청에서 예약 아이디를 찾아 유효하지 않으면 에러 발생
        Reservation reservation = reservationRepository.findById(changeRequest.getReservationId())
                .orElseThrow(() -> new ReservationNotFoundException());

        // 인증키에 있는 아이디와 요청하려는 예약의 스토어를 소유한 파트너의 아이디가 다르면 에러 발생
        if(!username.equals(reservation.getStore().getPartner().getUsername())){
            throw new AccessDeniedException();
        }

        // 이미 방문한 예약의 상태 거절 불가능
        // 이미 거절한 예약의 상태 거절 불가능
        if(reservation.getStatus() == ReservationStatus.VISITED){
            throw new AlreadyVisitedReservationException();
        } else if(reservation.getStatus() == ReservationStatus.DENIED){
            throw new AlreadyDeniedReservationException();
        }

        // 찾은 예약의 상태를 예약 거부 상태로 변경
        reservation.updateReservationStatus(ReservationStatus.DENIED);

        return ReservationDto.fromEntity(reservationRepository.save(reservation));
    }

    // 예약 방문
    @Transactional
    public ReservationDto reservationVisit(ReservationRequest.ChangeStatus changeRequest, String username){
        // 요청에서 예약 아이디를 찾아 유효하지 않으면 에러 발생
        Reservation reservation = reservationRepository.findById(changeRequest.getReservationId())
                .orElseThrow(() -> new ReservationNotFoundException());

        // 인증키에 있는 아이디와 요청하려는 예약의 멤버 아이디가 다르면 에러 발생
        if(!username.equals(reservation.getMember().getUsername())){
            throw new AccessDeniedException();
        }

        // 이미 방문한 예약의 상태 변경 불가능
        // 아직 방문 하지 않았고 예약의 상태가 승인 되지 않은 상태라면 방문이 불가능
        if(reservation.getStatus() == ReservationStatus.VISITED){
            throw new AlreadyVisitedReservationException();
        } else if(reservation.getStatus() != ReservationStatus.APPROVED){
            throw new ReservationNotVisitedException();
        }

        // 찾은 예약의 상태를 가게방문 상태로 변경
        reservation.updateReservationStatus(ReservationStatus.VISITED);

        return ReservationDto.fromEntity(reservationRepository.save(reservation));
    }

}

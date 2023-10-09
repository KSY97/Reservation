package com.zerobase.reservation.service;

import com.zerobase.reservation.constants.ReservationStatus;
import com.zerobase.reservation.constants.ReviewScore;
import com.zerobase.reservation.dto.ReviewDto;
import com.zerobase.reservation.dto.ReviewRequest;
import com.zerobase.reservation.exception.impl.*;
import com.zerobase.reservation.repository.MemberRepository;
import com.zerobase.reservation.repository.ReservationRepository;
import com.zerobase.reservation.repository.ReviewRepository;
import com.zerobase.reservation.repository.StoreRepository;
import com.zerobase.reservation.repository.entity.Member;
import com.zerobase.reservation.repository.entity.Reservation;
import com.zerobase.reservation.repository.entity.Review;
import com.zerobase.reservation.repository.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;

    // 리뷰 작성
    @Transactional
    public ReviewDto writeReview(ReviewRequest.Write reviewRequest, String username){
        // 요청에서 예약 아이디를 찾아 유효하지 않으면 에러 발생
        Reservation reservation = reservationRepository.findById(reviewRequest.getReservationId())
                .orElseThrow(() -> new ReservationNotFoundException());


        // 인증키에 있는 아이디와 요청하려는 예약의 멤버 아이디가 다르면 에러 발생
        if(!username.equals(reservation.getMember().getUsername())){
            throw new AccessDeniedException();
        }

        // 방문한 예약이 아니라면 리뷰 작성 불가
        if(reservation.getStatus() != ReservationStatus.VISITED){
            throw new ReservationNotVisitedException();
        }

        boolean exists = reviewRepository.existsByReservation(reservation);
        if(exists) throw new AlreadyWroteReviewException();

        return ReviewDto.fromEntity(reviewRepository.save(reviewRequest.toEntity(reservation)));
    }

    // 리뷰 수정
    @Transactional
    public ReviewDto editReview(ReviewRequest.Edit editRequest, String username){
        // 요청에서 리뷰 아이디를 찾아 유효하지 않으면 에러 발생
        Review review = reviewRepository.findById(editRequest.getReviewId())
                .orElseThrow(() -> new ReviewNotFoundException());

        // 인증키에 있는 아이디와 요청하려는 리뷰 예약의 멤버 아이디가 다르면 에러 발생
        if(!username.equals(review.getReservation().getMember().getUsername())){
            throw new AccessDeniedException();
        }

        // 평점의 기본값을 가져온 리뷰의 평점으로 초기화
        ReviewScore grade = review.getScore();

        // 요청한 평점으로 평점 수정
        switch (editRequest.getScore()) {
            case 1:
                grade = ReviewScore.POOR;
                break;
            case 2:
                grade = ReviewScore.BELOW_AVERAGE;
                break;
            case 3:
                grade = ReviewScore.AVERAGE;
                break;
            case 4:
                grade = ReviewScore.ABOVE_AVERAGE;
                break;
            case 5:
                grade = ReviewScore.EXCELLENT;
                break;
            default:
        }

        // 리뷰를 새로 요청한 내용으로 수정
        review.updateReview(grade, editRequest.getContents());

        return ReviewDto.fromEntity(reviewRepository.save(review));
    }
    
    // 리뷰 삭제
    @Transactional
    public String deleteReview(ReviewRequest.Delete deleteRequest, String username){
        // 요청에서 리뷰 아이디를 찾아 유효하지 않으면 에러 발생
        Review review = reviewRepository.findById(deleteRequest.getReviewId())
                .orElseThrow(() -> new ReviewNotFoundException());

        // 인증키에 있는 아이디와 요청하려는 리뷰 예약의 멤버 아이디가 다르면 에러 발생
        if(!username.equals(review.getReservation().getMember().getUsername())){
            throw new AccessDeniedException();
        }

        // 리뷰 삭제
        reviewRepository.delete(review);

        return "delete review success";
    }

    // 멤버 아이디로 리뷰 목록 가져오기
    public List<ReviewDto> readReviewByMemberId(Long memberId){
        // 요청에서 멤버 아이디를 찾아 유효하지 않으면 에러 발생
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UserNotFoundException());

        // 찾은 멤버 아이디에 있는 모든 예약을 가져옴
        List<Reservation> reservations = reservationRepository.findByMember(member);

        // 찾은 예약들의 리뷰를 가져옴
        List<Review> reviews = reservations.stream()
                .map(reviewRepository::findByReservation)
                .collect(Collectors.toList());

        return reviews.stream()
                .map(ReviewDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 스토어 아이디로 리뷰 목록 가져오기
    public List<ReviewDto> readReviewByStoreId(Long storeId){
        // 요청에서 스토어 아이디를 찾아 유효하지 않으면 에러 발생
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException());

        // 찾은 스토어 아이디에 있는 모든 예약을 가져옴
        List<Reservation> reservations = reservationRepository.findByStore(store);

        // 찾은 예약들의 리뷰를 가져옴
        List<Review> reviews = reservations.stream()
                .map(reviewRepository::findByReservation)
                .collect(Collectors.toList());

        return reviews.stream()
                .map(ReviewDto::fromEntity)
                .collect(Collectors.toList());
    }

}

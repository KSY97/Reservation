package com.zerobase.reservation.controller;

import com.zerobase.reservation.dto.ReviewDto;
import com.zerobase.reservation.dto.ReviewRequest;
import com.zerobase.reservation.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    // 리뷰작성관련 동작을 위한 컨트롤러

    private final ReviewService reviewService;

    // 방문 완료한 예약에 대해서 리뷰 작성
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping
    public ResponseEntity<?> writeReview(
            @Valid @RequestBody ReviewRequest.Write request,
            Principal principal
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(reviewService.writeReview(request, principal.getName()));
    }

    // 리뷰 수정
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PutMapping
    public ResponseEntity<?> editReview(
            @Valid @RequestBody ReviewRequest.Edit request,
            Principal principal
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(reviewService.editReview(request, principal.getName()));
    }

    // 리뷰 삭제
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @DeleteMapping("/delete")
    public String deleteReview(
            @RequestBody ReviewRequest.Delete request,
            Principal principal
    ){
        return reviewService.deleteReview(request, principal.getName());
    }

    // 멤버 리뷰 조회
    @GetMapping("/read/member")
    public List<ReviewDto> readReviewByMemberId(
            @RequestParam("member_id") Long memberId
    ){
        return reviewService.readReviewByMemberId(memberId);
    }

    // 스토어 리뷰 조회
    @GetMapping("/read/store")
    public List<ReviewDto> readReviewByStoreId(
            @RequestParam("store_id") Long storeId
    ){
        return reviewService.readReviewByStoreId(storeId);
    }

}

package com.zerobase.reservation.dto;

import com.zerobase.reservation.repository.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {

    private Long reviewId;
    private Long reservationId;
    private String grade;
    private String contents;
    private LocalDateTime modifiedAt;
    private LocalDateTime reviewedAt;

    public static ReviewDto fromEntity(Review review){
        return ReviewDto.builder()
                .reviewId(review.getId())
                .reservationId(review.getReservation().getId())
                .grade(review.getScore().getGrade())
                .contents(review.getContents())
                .modifiedAt(review.getModifiedAt())
                .reviewedAt(review.getReviewedAt())
                .build();
    }
}

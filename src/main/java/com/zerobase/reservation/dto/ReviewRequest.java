package com.zerobase.reservation.dto;

import com.zerobase.reservation.constants.ReviewScore;
import com.zerobase.reservation.repository.entity.Reservation;
import com.zerobase.reservation.repository.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

public class ReviewRequest {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Write {

        private Long reservationId;
        @Min(value = 1, message = "점수는 1점 이상, 5점 이하만 가능합니다.")
        @Max(value = 5, message = "점수는 1점 이상, 5점 이하만 가능합니다.")
        private Integer score;
        private String contents;

        public Review toEntity(Reservation reservation) {
            ReviewScore grade = ReviewScore.EXCELLENT;

            switch (this.score){
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
                    break;
            }
            return Review.builder()
                    .reservation(reservation)
                    .score(grade)
                    .contents(this.contents)
                    .reviewedAt(LocalDateTime.now())
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Edit {
        private Long reviewId;
        @Min(value = 1, message = "점수는 1점 이상, 5점 이하만 가능합니다.")
        @Max(value = 5, message = "점수는 1점 이상, 5점 이하만 가능합니다.")
        private Integer score;
        private String contents;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Delete {
        private Long reviewId;
    }
}

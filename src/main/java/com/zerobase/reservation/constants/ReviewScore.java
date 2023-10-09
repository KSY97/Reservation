package com.zerobase.reservation.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ReviewScore {
    // 리뷰 점수

    POOR("Poor", 1),
    BELOW_AVERAGE("Below Average",  2),
    AVERAGE("Average",  3),
    ABOVE_AVERAGE("Above Average",  4),
    EXCELLENT("Excellent",  5);

    private String grade;
    private Integer score;

    public static ReviewScore ofCode(Integer dbData){
        return Arrays.stream(ReviewScore.values())
                .filter(v -> v.getScore().equals(dbData))
                .findAny()
                .orElseThrow(() -> new RuntimeException("존재하지 않는 예약 상태 코드입니다."));
    }
}

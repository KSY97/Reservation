package com.zerobase.reservation.constants.converter;

import com.zerobase.reservation.constants.ReviewScore;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ScoreConverter implements AttributeConverter<ReviewScore, Integer> {
    // 리뷰 점수를 나타내기 위한 enum 형식의 ReviewScore 를 db 컬럼에선 숫자코드로 나타내기 위한 컨버터

    @Override
    public Integer convertToDatabaseColumn(ReviewScore score) {
        if(score == null) return null;
        return score.getScore();
    }

    @Override
    public ReviewScore convertToEntityAttribute(Integer dbData) {
        if(dbData == null) return null;
        return ReviewScore.ofCode(dbData);
    }
}

package com.zerobase.reservation.repository.entity;

import com.sun.istack.NotNull;
import com.zerobase.reservation.constants.ReviewScore;
import com.zerobase.reservation.constants.converter.ScoreConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Convert(converter = ScoreConverter.class)
    private ReviewScore score;

    private String contents;

    @OneToOne
    private Reservation reservation;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @CreatedDate
    private LocalDateTime reviewedAt;

    public void updateReview(ReviewScore score, String contents){
        this.score = score;
        this.contents = contents;
        this.modifiedAt = LocalDateTime.now();
    }

}

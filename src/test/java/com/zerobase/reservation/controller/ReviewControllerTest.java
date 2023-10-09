package com.zerobase.reservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.reservation.constants.ReviewScore;
import com.zerobase.reservation.dto.ReviewDto;
import com.zerobase.reservation.dto.ReviewRequest;
import com.zerobase.reservation.security.TokenProvider;
import com.zerobase.reservation.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
public class ReviewControllerTest {

    @MockBean
    private ReviewService reviewService ;

    @MockBean
    private TokenProvider tokenProvider;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @Transactional
    void successWriteReview() throws Exception {
        //given
        ReviewRequest.Write writeRequest = new ReviewRequest.Write(1L, 5, "review contents");

        given(reviewService.writeReview(any(), anyString()))
                .willReturn(ReviewDto.builder()
                        .reviewId(1L)
                        .reservationId(1L)
                        .grade(ReviewScore.EXCELLENT.getGrade())
                        .contents("review contents")
                        .build());
        //when
        //then
        mockMvc.perform(post("/review")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(writeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewId").value("1"))
                .andExpect(jsonPath("$.reservationId").value("1"))
                .andExpect(jsonPath("$.grade").value("Excellent"))
                .andExpect(jsonPath("$.contents").value("review contents"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    @Transactional
    void successEditReview() throws Exception {
        //given
        ReviewRequest.Edit editRequest = new ReviewRequest.Edit(1L, 3, "edited review contents");

        given(reviewService.editReview(any(), anyString()))
                .willReturn(ReviewDto.builder()
                        .reviewId(1L)
                        .reservationId(1L)
                        .grade(ReviewScore.AVERAGE.getGrade())
                        .contents("edited review contents")
                        .build());
        //when
        //then
        mockMvc.perform(put("/review")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewId").value("1"))
                .andExpect(jsonPath("$.reservationId").value("1"))
                .andExpect(jsonPath("$.grade").value("Average"))
                .andExpect(jsonPath("$.contents").value("edited review contents"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    @Transactional
    void successDeleteReview() throws Exception {
        //given
        ReviewRequest.Delete deleteRequest = new ReviewRequest.Delete(1L);

        given(reviewService.deleteReview(any(), anyString()))
                .willReturn("delete review success");
        //when
        //then
        mockMvc.perform(post("/review/delete")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("delete review success"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    void successGetReviewByMemberId() throws Exception {
        //given
        List<ReviewDto> reviewDtos =
                Arrays.asList(
                        ReviewDto.builder()
                                .reviewId(1L)
                                .reservationId(1L)
                                .grade(ReviewScore.AVERAGE.getGrade())
                                .contents("review contents1").build(),
                        ReviewDto.builder()
                                .reviewId(2L)
                                .reservationId(2L)
                                .grade(ReviewScore.EXCELLENT.getGrade())
                                .contents("review contents2").build()
                );

        given(reviewService.readReviewByMemberId(any()))
                .willReturn(reviewDtos);
        //when
        //then
        mockMvc.perform(get("/review/read/member?member_id=1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(jsonPath("$[0].reviewId").value("1"))
                .andExpect(jsonPath("$[0].reservationId").value("1"))
                .andExpect(jsonPath("$[0].grade").value("Average"))
                .andExpect(jsonPath("$[0].contents").value("review contents1"))
                .andExpect(jsonPath("$[1].reviewId").value("2"))
                .andExpect(jsonPath("$[1].reservationId").value("2"))
                .andExpect(jsonPath("$[1].grade").value("Excellent"))
                .andExpect(jsonPath("$[1].contents").value("review contents2"));
    }

    @Test
    @WithMockUser
    void successGetReviewByStoreId() throws Exception {
        //given
        List<ReviewDto> reviewDtos =
                Arrays.asList(
                        ReviewDto.builder()
                                .reviewId(1L)
                                .reservationId(1L)
                                .grade(ReviewScore.AVERAGE.getGrade())
                                .contents("review contents1").build(),
                        ReviewDto.builder()
                                .reviewId(2L)
                                .reservationId(2L)
                                .grade(ReviewScore.EXCELLENT.getGrade())
                                .contents("review contents2").build()
                );

        given(reviewService.readReviewByStoreId(any()))
                .willReturn(reviewDtos);
        //when
        //then
        mockMvc.perform(get("/review/read/store?store_id=1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(jsonPath("$[0].reviewId").value("1"))
                .andExpect(jsonPath("$[0].reservationId").value("1"))
                .andExpect(jsonPath("$[0].grade").value("Average"))
                .andExpect(jsonPath("$[0].contents").value("review contents1"))
                .andExpect(jsonPath("$[1].reviewId").value("2"))
                .andExpect(jsonPath("$[1].reservationId").value("2"))
                .andExpect(jsonPath("$[1].grade").value("Excellent"))
                .andExpect(jsonPath("$[1].contents").value("review contents2"));
    }
}

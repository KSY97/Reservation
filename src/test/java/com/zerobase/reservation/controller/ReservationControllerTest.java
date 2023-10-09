package com.zerobase.reservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.reservation.dto.ReservationDto;
import com.zerobase.reservation.dto.ReservationRequest;
import com.zerobase.reservation.security.TokenProvider;
import com.zerobase.reservation.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
public class ReservationControllerTest {

    @MockBean
    private ReservationService reservationService ;

    @MockBean
    private TokenProvider tokenProvider;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @Transactional
    void successReserveStore() throws Exception {
        //given
        ReservationRequest.Reserve reserveRequest = new ReservationRequest.Reserve(1L, 1L, LocalDateTime.now().plusDays(1));

        given(reservationService.reservation(any(), anyString()))
                .willReturn(ReservationDto.builder()
                        .reservationId(1L)
                        .memberName("member_name")
                        .storeName("store_name")
                        .status("Unapproved")
                        .reservationDate(LocalDateTime.now().plusDays(1))
                        .reservatedAt(LocalDateTime.now())
                        .build());
        //when
        //then
        mockMvc.perform(post("/reservation")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reserveRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").value("1"))
                .andExpect(jsonPath("$.memberName").value("member_name"))
                .andExpect(jsonPath("$.storeName").value("store_name"))
                .andExpect(jsonPath("$.status").value("Unapproved"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    void successGetReservationByMemberId() throws Exception {
        //given
        List<ReservationDto> reservationDtos =
                Arrays.asList(
                        ReservationDto.builder()
                                .memberName("member_name1")
                                .storeName("store_name1")
                                .reservationDate(LocalDateTime.now().plusDays(1))
                                .reservatedAt(LocalDateTime.now()).build(),
                        ReservationDto.builder()
                                .memberName("member_name2")
                                .storeName("store_name2")
                                .reservationDate(LocalDateTime.now().plusDays(2))
                                .reservatedAt(LocalDateTime.now()).build(),
                        ReservationDto.builder()
                                .memberName("member_name3")
                                .storeName("store_name3")
                                .reservationDate(LocalDateTime.now().plusDays(3))
                                .reservatedAt(LocalDateTime.now()).build()
                );

        given(reservationService.getReservationByMemberId(anyLong(), anyInt(), anyString()))
                .willReturn(reservationDtos);

        //when
        //then
        mockMvc.perform(get("/reservation/member?member_id=1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(jsonPath("$[0].memberName").value("member_name1"))
                .andExpect(jsonPath("$[0].storeName").value("store_name1"))
                .andExpect(jsonPath("$[1].memberName").value("member_name2"))
                .andExpect(jsonPath("$[1].storeName").value("store_name2"))
                .andExpect(jsonPath("$[2].memberName").value("member_name3"))
                .andExpect(jsonPath("$[2].storeName").value("store_name3"));
    }

    @Test
    @WithMockUser
    void successGetReservationByStoreId() throws Exception {
        //given
        List<ReservationDto> reservationDtos =
                Arrays.asList(
                        ReservationDto.builder()
                                .memberName("member_name1")
                                .storeName("store_name1")
                                .reservationDate(LocalDateTime.now().plusDays(1))
                                .reservatedAt(LocalDateTime.now()).build(),
                        ReservationDto.builder()
                                .memberName("member_name2")
                                .storeName("store_name2")
                                .reservationDate(LocalDateTime.now().plusDays(2))
                                .reservatedAt(LocalDateTime.now()).build(),
                        ReservationDto.builder()
                                .memberName("member_name3")
                                .storeName("store_name3")
                                .reservationDate(LocalDateTime.now().plusDays(3))
                                .reservatedAt(LocalDateTime.now()).build()
                );

        given(reservationService.getReservationByStoreIdOrderByReservationDate(anyLong(), anyInt(), anyString()))
                .willReturn(reservationDtos);

        //when
        //then
        mockMvc.perform(get("/reservation/store?store_id=1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(jsonPath("$[0].memberName").value("member_name1"))
                .andExpect(jsonPath("$[0].storeName").value("store_name1"))
                .andExpect(jsonPath("$[1].memberName").value("member_name2"))
                .andExpect(jsonPath("$[1].storeName").value("store_name2"))
                .andExpect(jsonPath("$[2].memberName").value("member_name3"))
                .andExpect(jsonPath("$[2].storeName").value("store_name3"));
    }

    @Test
    @WithMockUser
    @Transactional
    void successReservationApprove() throws Exception {
        //given
        ReservationRequest.ChangeStatus changeRequest = new ReservationRequest.ChangeStatus(1L);

        given(reservationService.reservationAccessApprove(any(), anyString()))
                .willReturn(ReservationDto.builder()
                        .reservationId(1L)
                        .memberName("member_name")
                        .storeName("store_name")
                        .status("Approved")
                        .reservationDate(LocalDateTime.now().plusDays(1))
                        .reservatedAt(LocalDateTime.now())
                        .build());

        //when
        //then
        mockMvc.perform(put("/reservation/approve")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").value("1"))
                .andExpect(jsonPath("$.memberName").value("member_name"))
                .andExpect(jsonPath("$.storeName").value("store_name"))
                .andExpect(jsonPath("$.status").value("Approved"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    @Transactional
    void successReservationDeny() throws Exception {
        //given
        ReservationRequest.ChangeStatus changeRequest = new ReservationRequest.ChangeStatus(1L);

        given(reservationService.reservationAccessDeny(any(), anyString()))
                .willReturn(ReservationDto.builder()
                        .reservationId(1L)
                        .memberName("member_name")
                        .storeName("store_name")
                        .status("Denied")
                        .reservationDate(LocalDateTime.now().plusDays(1))
                        .reservatedAt(LocalDateTime.now())
                        .build());

        //when
        //then
        mockMvc.perform(put("/reservation/deny")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").value("1"))
                .andExpect(jsonPath("$.memberName").value("member_name"))
                .andExpect(jsonPath("$.storeName").value("store_name"))
                .andExpect(jsonPath("$.status").value("Denied"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    @Transactional
    void successReservationVisit() throws Exception {
        //given
        ReservationRequest.ChangeStatus changeRequest = new ReservationRequest.ChangeStatus(1L);

        given(reservationService.reservationVisit(any(), anyString()))
                .willReturn(ReservationDto.builder()
                        .reservationId(1L)
                        .memberName("member_name")
                        .storeName("store_name")
                        .status("Visited")
                        .reservationDate(LocalDateTime.now().plusDays(1))
                        .reservatedAt(LocalDateTime.now())
                        .build());

        //when
        //then
        mockMvc.perform(put("/reservation/visit")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").value("1"))
                .andExpect(jsonPath("$.memberName").value("member_name"))
                .andExpect(jsonPath("$.storeName").value("store_name"))
                .andExpect(jsonPath("$.status").value("Visited"))
                .andDo(print());
    }

}

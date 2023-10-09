package com.zerobase.reservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.reservation.dto.StoreDto;
import com.zerobase.reservation.dto.StoreRequest;
import com.zerobase.reservation.security.TokenProvider;
import com.zerobase.reservation.service.StoreService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StoreController.class)
public class StoreControllerTest {

    @MockBean
    private StoreService storeService;

    @MockBean
    private TokenProvider tokenProvider;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @Transactional
    void successRegisterStore() throws Exception {
        //given
        StoreRequest.Register storeRequest = new StoreRequest.Register("name", "address", "detail", 1L);

        given(storeService.register(any(), anyString()))
                .willReturn(StoreDto.builder()
                        .storeId(1L)
                        .storeName("store-name")
                        .address("address")
                        .detail("detail")
                        .build());
        //when
        //then
        mockMvc.perform(post("/store/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(storeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeId").value("1"))
                .andExpect(jsonPath("$.storeName").value("store-name"))
                .andExpect(jsonPath("$.address").value("address"))
                .andExpect(jsonPath("$.detail").value("detail"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    void successGetStoresByPartnerId() throws Exception {
        //given
        List<StoreDto> storeDtos =
                Arrays.asList(
                        StoreDto.builder()
                                .storeName("store_name1")
                                .address("서울시 송파구 방이동").build(),
                        StoreDto.builder()
                                .storeName("store_name2")
                                .address("서울시 강동구").build(),
                        StoreDto.builder()
                                .storeName("store_name3")
                                .address("서울시 송파구 잠실동").build()
                );

        given(storeService.getStoresByPartnerId(anyLong()))
                .willReturn(storeDtos);

        //when
        //then
        mockMvc.perform(get("/store?partner_id=1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(jsonPath("$[0].storeName").value("store_name1"))
                .andExpect(jsonPath("$[0].address").value("서울시 송파구 방이동"))
                .andExpect(jsonPath("$[1].storeName").value("store_name2"))
                .andExpect(jsonPath("$[1].address").value("서울시 강동구"))
                .andExpect(jsonPath("$[2].storeName").value("store_name3"))
                .andExpect(jsonPath("$[2].address").value("서울시 송파구 잠실동"));
    }
}

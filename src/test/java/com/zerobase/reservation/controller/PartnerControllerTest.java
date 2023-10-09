package com.zerobase.reservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.reservation.constants.Authority;
import com.zerobase.reservation.dto.AuthPartner;
import com.zerobase.reservation.dto.PartnerDto;
import com.zerobase.reservation.security.TokenProvider;
import com.zerobase.reservation.service.PartnerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PartnerController.class)
public class PartnerControllerTest {

    @MockBean
    private PartnerService partnerService;

    @MockBean
    private TokenProvider tokenProvider;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @Transactional
    void successRegisterPartner() throws Exception {
        //given
        AuthPartner.SignUp partnerRequest = new AuthPartner.SignUp("partner", "asd1234");
        Set<String> roles = new HashSet<>();
        roles.add(Authority.ROLE_PARTNER.name());

        given(partnerService.register(any()))
                .willReturn(PartnerDto.builder()
                        .partnerId(1L)
                        .partnerUsername("partner")
                        .roles(roles)
                        .registeredAt(LocalDateTime.now())
                        .build());
        //when
        //then
        mockMvc.perform(post("/partner/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partnerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.partnerId").value("1"))
                .andExpect(jsonPath("$.partnerUsername").value("partner"))
                .andExpect(jsonPath("$.roles").value("ROLE_PARTNER"))
                .andDo(print());
    }
}

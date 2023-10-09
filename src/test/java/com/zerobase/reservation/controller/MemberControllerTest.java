package com.zerobase.reservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.reservation.constants.Authority;
import com.zerobase.reservation.dto.AuthMember;
import com.zerobase.reservation.dto.MemberDto;
import com.zerobase.reservation.security.TokenProvider;
import com.zerobase.reservation.service.MemberService;
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

@WebMvcTest(MemberController.class)
public class MemberControllerTest {

    @MockBean
    private MemberService memberService;

    @MockBean
    private TokenProvider tokenProvider;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @Transactional
    void successRegisterMember() throws Exception {
        //given
        AuthMember.SignUp memberRequest = new AuthMember.SignUp("member", "asd1234", "010-1234-5678");
        Set<String> roles = new HashSet<>();
        roles.add(Authority.ROLE_MEMBER.name());
        given(memberService.register(any()))
                .willReturn(MemberDto.builder()
                        .memberId(1L)
                        .memberUsername("member")
                        .phoneNum("010-1234-5678")
                        .roles(roles)
                        .registeredAt(LocalDateTime.now())
                        .build());
        //when
        //then
        mockMvc.perform(post("/member/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value("1"))
                .andExpect(jsonPath("$.memberUsername").value("member"))
                .andExpect(jsonPath("$.phoneNum").value("010-1234-5678"))
                .andExpect(jsonPath("$.roles").value("ROLE_MEMBER"))
                .andDo(print());
    }
}

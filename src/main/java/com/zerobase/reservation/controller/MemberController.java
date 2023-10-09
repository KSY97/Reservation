package com.zerobase.reservation.controller;

import com.zerobase.reservation.dto.AuthMember;
import com.zerobase.reservation.repository.entity.Member;
import com.zerobase.reservation.security.TokenProvider;
import com.zerobase.reservation.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    // 일반 사용자들을 위한 컨트롤러

    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    // 멤버 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(
            @Valid @RequestBody AuthMember.SignUp request
            ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberService.register(request));
    }

    // 멤버 로그인
    @PostMapping("/signin")
    public ResponseEntity<?> signin(
            @RequestBody AuthMember.SignIn request
    ){
        Member member = memberService.authenticate(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(tokenProvider.generateToken(member.getUsername(), member.getRoles()));
    }
}

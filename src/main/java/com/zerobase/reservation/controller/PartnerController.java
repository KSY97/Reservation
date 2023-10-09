package com.zerobase.reservation.controller;

import com.zerobase.reservation.dto.AuthPartner;
import com.zerobase.reservation.repository.entity.Partner;
import com.zerobase.reservation.security.TokenProvider;
import com.zerobase.reservation.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/partner")
@RequiredArgsConstructor
public class PartnerController {
    // 파트너 사용자(가게 주인)들를 위한 컨트롤러

    private final PartnerService partnerService;
    private final TokenProvider tokenProvider;

    // 파트너 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(
            @Valid @RequestBody AuthPartner.SignUp request
            ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(partnerService.register(request));
    }

    // 파트너 로그인
    @PostMapping("/signin")
    public ResponseEntity<?> signin(
            @RequestBody AuthPartner.SignIn request
    ){
        Partner partner = partnerService.authenticate(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(tokenProvider.generateToken(partner.getUsername(), partner.getRoles()));
    }

}

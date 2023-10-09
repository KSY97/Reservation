package com.zerobase.reservation.dto;

import com.zerobase.reservation.constants.Authority;
import com.zerobase.reservation.repository.entity.Partner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class AuthPartner {

    @Data
    public static class SignIn {

        private String username;
        private String password;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignUp {

        @NotBlank(message = "아이디는 필수 입력값입니다.")
        @Pattern (regexp = "^[a-zA-Z0-9]*$", message = "사용자이름은 영어랑 숫자만 가능합니다.")
        private String username;

        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        private String password;

        public Partner toEntity() {
            Set<String> roles = new HashSet<>();
            roles.add(Authority.ROLE_PARTNER.name());
            return Partner.builder()
                    .username(this.username)
                    .password(this.password)
                    .roles(roles)
                    .registeredAt(LocalDateTime.now())
                    .build();
        }
    }
}

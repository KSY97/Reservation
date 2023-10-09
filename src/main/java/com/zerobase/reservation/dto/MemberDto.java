package com.zerobase.reservation.dto;

import com.zerobase.reservation.repository.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {

    private Long memberId;
    private String memberUsername;
    private String phoneNum;
    private Set<String> roles;
    private LocalDateTime registeredAt;

    public static MemberDto fromEntity(Member member){
        return MemberDto.builder()
                .memberId(member.getId())
                .memberUsername(member.getUsername())
                .phoneNum(member.getPhoneNum())
                .roles(member.getRoles())
                .registeredAt(member.getRegisteredAt())
                .build();
    }
}

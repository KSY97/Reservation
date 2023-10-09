package com.zerobase.reservation.dto;

import com.zerobase.reservation.repository.entity.Partner;
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
public class PartnerDto {

    private Long partnerId;
    private String partnerUsername;
    private Set<String> roles;
    private LocalDateTime registeredAt;

    public static PartnerDto fromEntity(Partner partner){
        return PartnerDto.builder()
                .partnerId(partner.getId())
                .partnerUsername(partner.getUsername())
                .roles(partner.getRoles())
                .registeredAt(partner.getRegisteredAt())
                .build();
    }
}

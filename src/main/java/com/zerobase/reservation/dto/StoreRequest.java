package com.zerobase.reservation.dto;

import com.zerobase.reservation.repository.entity.Partner;
import com.zerobase.reservation.repository.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class StoreRequest {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Register {

        private String name;
        private String address;
        private String detail;
        private Long partnerId;

        public Store toEntity(Partner partner) {
            return Store.builder()
                    .name(this.name)
                    .address(this.address)
                    .detail(this.detail)
                    .partner(partner)
                    .build();
        }
    }
}

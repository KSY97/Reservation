package com.zerobase.reservation.dto;

import com.zerobase.reservation.repository.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreDto {

    private Long storeId;
    private String storeName;
    private String address;
    private String detail;

    public static StoreDto fromEntity(Store store){
        return StoreDto.builder()
                .storeId(store.getId())
                .storeName(store.getName())
                .address(store.getAddress())
                .detail(store.getDetail())
                .build();
    }
}

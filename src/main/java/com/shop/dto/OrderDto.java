package com.shop.dto;

import com.shop.domain.CartItem;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class OrderDto {

    @NotNull(message = "상품 아이디는 필수 입력 값입니다.")
    private Long ItemId;

    @Min(value = 1, message = "최소 주문 수량은 1개 입니다.")
    @Max(value = 999, message = "최대 주문 수량은 999개 입니다.")
    private int count;

    @Builder
    public OrderDto(Long itemId, int count) {
        ItemId = itemId;
        this.count = count;
    }

    public static OrderDto from(CartItem cartItem){

        return OrderDto.builder()
                .itemId(cartItem.getItem().getId())
                .count(cartItem.getCount())
                .build();
    }
}

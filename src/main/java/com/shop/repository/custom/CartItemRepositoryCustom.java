package com.shop.repository.custom;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.domain.QCartItem;
import com.shop.domain.QItem;
import com.shop.domain.QItemImg;
import com.shop.dto.CartDetailDto;
import com.shop.enums.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CartItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QCartItem cartItem = new QCartItem("cartItem");
    QItem item = new QItem("item");
    QItemImg itemImg = new QItemImg("itemImg");

    public List<CartDetailDto> getCartDetailList(Long cartId){

         return queryFactory
                .select(Projections.fields(
                        CartDetailDto.class,
                        cartItem.id.as("cartItemId")
                        , item.itemName.as("itemName")
                        , item.price
                        , cartItem.count
                        , itemImg.imgUrl
                        )
                )
                .from(cartItem)
                .join(cartItem.item, item)
                .join(itemImg)
                    .on(itemImg.item.id.eq(item.id))

                .where(
                        cartItem.cart.id.eq(cartId),
                        itemImg.repImgYn.eq(StatusEnum.FLAG_Y.getValue())
                )

                .orderBy(cartItem.createTime.desc())
                .fetch();

    }

}

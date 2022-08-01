package com.shop.repository.custom;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.domain.*;
import com.shop.dto.response.OrderDetailResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QOrder order = QOrder.order;
    QMember member = QMember.member;
    QOrderItem orderItem = QOrderItem.orderItem;
    QItem item = QItem.item;
    QItemImg itemImg = QItemImg.itemImg;

    public OrderDetailResponseDto getOrderDetail(String orderId){

        return queryFactory
                .select(Projections.fields(
                        OrderDetailResponseDto.class
                        , order.id.as("orderId")
                        , order.orderStatus.as("orderStatus")
                        , member.name.as("memberName")
                        , member.email.as("memberEmail")
                        , member.address.as("memberAddress")
                        , orderItem.count.as("orderCount")
                        , orderItem.orderPrice.as("orderPrice")
                        , item.itemName
                        , item.itemDetail
                        , itemImg.imgUrl
                        , itemImg.oriImgName.as("imgName")
                        , order.createTime
                ))

                .from(order)
                .join(order.member, member)
                .leftJoin(orderItem)
                .on(order.id.eq(orderItem.order.id))
                .join(orderItem.item, item)
                .join(itemImg)
                .on(item.id.eq(itemImg.item.id))

                .where(order.id.eq(Long.valueOf(orderId)))
                .fetchOne();
    }
}

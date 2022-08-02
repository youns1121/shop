package com.shop.repository.custom;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.domain.*;
import com.shop.dto.response.OrderItemResponseListDto;
import com.shop.dto.response.OrderMemberInfoResponseDto;
import com.shop.enums.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QOrder order = QOrder.order;
    QOrderItem orderItem = QOrderItem.orderItem;
    QItem item = QItem.item;
    QItemImg itemImg = QItemImg.itemImg;
    QMember member = QMember.member;

    public List<OrderItemResponseListDto> getOrderDetail(Long orderId){

        return queryFactory
                .select(Projections.fields(
                        OrderItemResponseListDto.class
                        , order.id.as("orderId")
                        , order.orderStatus.as("orderStatus")
                        , orderItem.count.as("orderCount")
                        , orderItem.orderPrice.as("orderPrice")
                        , item.itemName
                        , item.itemDetail
                        , itemImg.imgUrl
                        , itemImg.oriImgName.as("imgName")
                        , order.createTime
                ))

                .from(order)
                .leftJoin(orderItem)
                .on(order.id.eq(orderItem.order.id))
                .join(orderItem.item, item)
                .join(itemImg)
                .on(item.id.eq(itemImg.item.id)
                        .and(itemImg.repImgYn.eq(StatusEnum.FLAG_Y.getStatusMessage())))

                .where(order.id.eq(orderId))
                .fetch();
    }

    public OrderMemberInfoResponseDto getOrderMember(Long orderId){

        return queryFactory
                .select(Projections.fields(
                        OrderMemberInfoResponseDto.class
                        , member.name.as("memberName")
                        , member.email.as("memberEmail")
                        , member.address.as("memberAddress")
                ))
                .from(member)
                .join(order)
                .on(order.member.id.eq(member.id))

                .where(order.id.eq(orderId))
                .fetchOne();
    }
}

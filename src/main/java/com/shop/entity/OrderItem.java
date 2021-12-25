package com.shop.entity;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.shop.entity.comm.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Table(name = "order_item")
@Entity
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name ="order_price", nullable = false)
    private int orderPrice; //주문 가격

    @Column(name="order_count", nullable = false)
    private int count; //수량

    public static OrderItem createOrderItem(Item item, int count){

        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item); // 주문할 상품
        orderItem.setCount(count); // 주문 수량
        orderItem.setOrderPrice(item.getPrice()); // 현재 시간 기준 상품 가격을 주문 가격으로 세팅, 상품 가격은 상황에 따라 달라짐 ex)쿠폰, 할인

        item.removeStock(count); // 주문 수량만큼 상품의 재고 수량을 감소

        return orderItem;
    }

    public int getTotalPrice(){ // 주문 가격과 주문 수량을 곱해서 해당 상품을 주문한 총 가격을 계산하는 메서드

        return orderPrice * count;
    }

    public void cancel() { // 주문을 취소할 경우 주문 수량만큼 상품의 재고를 증가시키는 메소드

        this.getItem().addStock(count);
    }


}

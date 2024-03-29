package com.shop.domain;

import com.shop.domain.comm.BaseEntity;
import com.shop.enums.OrderStatus;
import lombok.*;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@NoArgsConstructor
@EqualsAndHashCode(of="id", callSuper = false)
@Setter
@Getter
@Table(name = "orders")
@Entity
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true) // 부모 엔티티의 영속성 상태 변화를 자식 엔티티에 모두 전이, 고아 객체 제거
    private List<OrderItem> orderItemList;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name="order_status")
    private OrderStatus orderStatus;

    @Builder
    public Order(Long id, Member member, List<OrderItem> orderItemList, LocalDateTime orderDate, OrderStatus orderStatus) {
        this.id = id;
        this.member = member;
        this.orderItemList = orderItemList;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
    }

    public void addOrderItem(OrderItem orderItem){// orderItems에는 주문 상품 정보들을 담아줌, orderItem 객체를 order 객체에 추가함
        orderItemList.add(orderItem);
        orderItem.setOrder(this); // Order 엔티티와 OrderItem 엔티티가 양방향 참조 관계, orderItem 객체에도 order 객체를 세팅

    }

    public static Order create(Member member, List<OrderItem> orderItemList){

        return Order.builder()
                .member(member) // 상품을 주문한 회원 세팅
                .orderItemList(orderItemList)  // 상품 페이지에서는 1개의 상품을 주문, 장바구니 페이지에서는 한 번에 여러개 상품을 주문
                .orderStatus(OrderStatus.ORDER) //주문 상태를 Order로 세팅
                .orderDate(LocalDateTime.now()) // 현재 주문 시간
                .build();
    }

    public int getTotalPrice() {//총 주문 금액을 구하는 메서드

        int totalPrice = 0;
        for(OrderItem orderItem : orderItemList){

            totalPrice += orderItem.getOrderPrice();
        }
        return totalPrice;
    }

    public void cancelOrder(){ //주문 상태를 취소 상태로 바꿔주는 메소드

        this.orderStatus = OrderStatus.ORDER_CANCEL;

        for(OrderItem orderItem : orderItemList){

            orderItem.cancel();
        }
    }
}

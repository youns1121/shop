package com.shop.entity;

import com.shop.entity.comm.BaseEntity;
import com.shop.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Table(name = "orders")
@Entity
public class Order extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="memeber_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true) // 부모 엔티티의 영속성 상태 변화를 자식 엔티티에 모두 전이, 고아 객체 제거
    private List<OrderItem> orderItemList = new ArrayList<>();

    @Column(name = "order_date")
    private LocalDateTime orderDate; //주문일

    @Enumerated(EnumType.STRING)
    @Column(name="order_status")
    private OrderStatus orderStatus;

    public void addOrderItem(OrderItem orderItem){// orderItems에는 주문 상품 정보들을 담아줌, orderItem 객체를 order 객체에 추가함
        orderItemList.add(orderItem);
        orderItem.setOrder(this); // Order 엔티티와 OrderItem 엔티티가 양방향 참조 관계, orderItem 객체에도 order 객체를 세팅

    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList){
        Order order = new Order();
        order.setMember(member); // 상품을 주문한 회원 세팅
        for(OrderItem orderItem : orderItemList){ // 상품 페이지에서는 1개의 상품을 주문, 장바구니 페이지에서는 한 번에 여러개 상품을 주문
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER); // 주문 상태를 Order로 세팅
        order.setOrderDate(LocalDateTime.now()); // 현재 주문 시간

        return order;
    }

    public int getTotalPrice() {//총 주문 금액을 구하는 메서드

        int totalPrice = 0;
        for(OrderItem orderItem : orderItemList){

            totalPrice += orderItem.getOrderPrice();
        }
        return totalPrice;
    }

    public void cancelOrder(){ //주문 상태를 취소 상태로 바꿔주는 메소드

        this.orderStatus = OrderStatus.CANCEL;

        for(OrderItem orderItem : orderItemList){

            orderItem.cancel();
        }
    }
}

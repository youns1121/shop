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
}

package com.shop.entity;

import com.shop.entity.comm.BaseEntity;
import com.shop.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "orders")
@Entity
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="memeber_id")
    private Member member;

    @Column(name = "order_date")
    private LocalDateTime orderDate; //주문일

    @Enumerated(EnumType.STRING)
    @Column(name="order_status", nullable = false)
    private OrderStatus orderStatus;
}

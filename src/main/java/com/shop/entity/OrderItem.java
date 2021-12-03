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

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name ="order_price", nullable = false)
    private int orderPrice; //주문 가격

    @Column(name="order_count", nullable = false)
    private int count; //수량



}

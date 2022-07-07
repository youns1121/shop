package com.shop.domain;

import com.shop.domain.comm.BaseEntity;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@EqualsAndHashCode(of="id")
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

    @Builder
    public OrderItem(Long id, Item item, Order order, int orderPrice, int count) {
        this.id = id;
        this.item = item;
        this.order = order;
        this.orderPrice = orderPrice;
        this.count = count;
    }

    public static OrderItem create(Item item, int count){

        item.removeStock(count);

        return  OrderItem.builder()
                .item(item)
                .count(count)
                .orderPrice(item.getPrice())
                .build();
    }

    public int getTotalPrice(){ // 주문 가격과 주문 수량을 곱해서 해당 상품을 주문한 총 가격을 계산하는 메서드

        return orderPrice * count;
    }


    public void cancel() { // 주문을 취소할 경우 주문 수량만큼 상품의 재고를 증가시키는 메소드

        this.getItem().addStock(count);
    }
}

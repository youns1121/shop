package com.shop.domain;

import com.shop.domain.comm.BaseEntity;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@EqualsAndHashCode(of="id", callSuper = false)
@Getter
@Table(name ="cart_item")
@Entity
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cart_item_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    @Column(name = "count", nullable = false)
    private int count;

    @Builder
    public CartItem(Long id, Cart cart, Item item, int count) {
        this.id = id;
        this.cart = cart;
        this.item = item;
        this.count = count;
    }

    public static CartItem createCartItem(Cart cart, Item item, int count){ // 장바구니에 담을 상품 엔티티 생성

        return CartItem.builder()
                .item(item)
                .cart(cart)
                .count(count)
                .build();
    }

    public void addCount(int count){ //장바구니에 기존에 담겨 있는 상품인데, 해당 상품을 추가로 장바구니에 담을 떄 기존 수량에 현재 담을 수량을 더해 줄때 사용
        this.count += count;
    }

    public void updateCount(int count){ // 현재 장바구니에 담겨있는 수량을 변경 메서드
        this.count = count;
    }
}

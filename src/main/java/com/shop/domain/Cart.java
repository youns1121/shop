package com.shop.domain;

import com.shop.domain.comm.BaseEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@EqualsAndHashCode(of="id")
@Getter
@Setter
@Table(name = "cart")
@Entity
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="cart_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    public static Cart createCart(Member member){ // 장바구니 생성

        Cart cart = new Cart();
        cart.setMember(member);

        return cart;
    }
}

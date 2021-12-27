package com.shop.service;


import com.shop.domain.Cart;
import com.shop.domain.CartItem;
import com.shop.domain.Item;
import com.shop.domain.Member;
import com.shop.dto.CartItemDto;
import com.shop.repository.CartItemRepository;
import com.shop.repository.CartRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@Service
@Transactional
public class CartService { // 장바구니에 상품을 담는 로직

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public Long addCart(CartItemDto cartItemDto, String email){

        Item item = itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);// 장바구니에 담을 상품 엔티티를 조회

        Member member = memberRepository.findByEmail(email); // 현재 로그인한 회원 엔티티 조회

        Cart cart = cartRepository.findByMemberId(member.getId());// 현재 로그인한 회원의 장바구니 엔티티 조회

        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());// 현재 상품이 장바구니에 이미 들어가 있는지 조회

        if(savedCartItem != null){
            savedCartItem.addCount(cartItemDto.getCount()); // 장바구니에 이미 있던 상품일 경우 기준 수량에 현재 장바구니에 담을 수량 만큼 더해줌

            return savedCartItem.getId();
        }else {
            CartItem cartItem =
                    CartItem.createCartItem(cart, item, cartItemDto.getCount()); // 장바구니, 상품 엔티티, 장바구니에 담을 수량을 이용해 CartItem 엔티티 생성
            cartItemRepository.save(cartItem); // 장바구니에 들어갈 상품을 저장

            return cartItem.getId();
        }


    }
}

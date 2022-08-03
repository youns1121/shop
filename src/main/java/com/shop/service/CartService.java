package com.shop.service;


import com.shop.domain.Cart;
import com.shop.domain.CartItem;
import com.shop.domain.Item;
import com.shop.domain.Member;
import com.shop.dto.CartDetailDto;
import com.shop.dto.CartItemDto;
import com.shop.dto.CartOrderDto;
import com.shop.dto.OrderDto;
import com.shop.repository.CartItemRepository;
import com.shop.repository.CartRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.custom.CartItemRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.shop.global.error.exception.ErrorCode.EMAIL_NOT_FOUND;
import static com.shop.global.error.exception.ErrorCode.ITEM_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class CartService { 

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemRepositoryCustom cartItemRepositoryCustom;
    private final OrderService orderService;

    @Transactional
    public Long addCart(CartItemDto cartItemDto, String email){

        Item item = itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);// 장바구니에 담을 상품 엔티티를 조회
        
        Member member = getMember(email);

        Cart cart = Optional.ofNullable(cartRepository.findByMemberId(member.getId()))
                .orElseGet(()-> getCart(member));// 현재 로그인한 회원의 장바구니 엔티티 조회


        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        if (isExistCart(cartItemDto, savedCartItem)){
            return savedCartItem.getId();
        }

        CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
        cartItemRepository.save(cartItem);

        return cartItem.getId();
    }

    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email){

        List<CartDetailDto> cartDetailDtoList =  Collections.emptyList();

        Member member = getMember(email);

        Cart cart = cartRepository.findByMemberId(member.getId());

        return cart == null ? cartDetailDtoList : cartItemRepositoryCustom.getCartDetailList(cart.getId());
    }

    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String email) {

        Member curMember = getMember(email);
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

        return StringUtils.equals(curMember.getEmail(), cartItem.getCart().getMember().getEmail());
    }

    @Transactional
    public void updateCartItemCount(Long cartItemId, int count){

        CartItem cartItem = getCartItem(cartItemId);
        cartItem.updateCount(count);
    }

    @Transactional
    public void deleteCartItem(Long cartItemId){

        cartItemRepository.delete(getCartItem(cartItemId));
    }

    @Transactional
    public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String email){

        deleteCartItem(cartOrderDtoList);

        return createOrder(cartOrderDtoList, email);
    }

    private void deleteCartItem(List<CartOrderDto> cartOrderDtoList) {
        for(CartOrderDto cartOrderDto : cartOrderDtoList){

            CartItem cartItem = getCartItem(cartOrderDto.getCartItemId());
            cartItemRepository.delete(cartItem);
        }
    }

    private CartItem getCartItem(Long cartItemId) {
        return cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException(ITEM_NOT_FOUND.getMessage()));
    }

    private Long createOrder(List<CartOrderDto> cartOrderDtoList, String email) {

        List<OrderDto> orderDtoList = new ArrayList<>();

        for (CartOrderDto cartOrderDto : cartOrderDtoList){

            CartItem cartItem = getCartItem(cartOrderDto.getCartItemId());

            orderDtoList.add(OrderDto.from(cartItem));
        }

        return orderService.orders(orderDtoList, email); // 장바구니에 담은 상품을 주문하도록 주문 로직을 호출
    }

    private boolean isExistCart(CartItemDto cartItemDto, CartItem savedCartItem) {
        Optional<CartItem> itemOptional = Optional.ofNullable(savedCartItem);

        if(itemOptional.isPresent()){

            savedCartItem.addCount(cartItemDto.getCount());
            return true;
        }

        return false;
    }

    private Cart getCart(Member member) {
        Cart cart;
        cart = Cart.createCart(member); // 상품을 처음으로 장바구니에 담을 경우 해당 회원의 장바구니 엔티티 생성
        cartRepository.save(cart);
        return cart;
    }

    private Member getMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(EMAIL_NOT_FOUND.getMessage()));
    }
}

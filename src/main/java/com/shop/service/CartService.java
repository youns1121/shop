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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CartService { // 장바구니에 상품을 담는 로직

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderService orderService;

    /**
     * 장바구니 담기
     * @param cartItemDto
     * @param email
     * @return
     */
    @Transactional
    public Long addCart(CartItemDto cartItemDto, String email){

        Item item = itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);// 장바구니에 담을 상품 엔티티를 조회

        Member member = memberRepository.findByEmail(email); // 현재 로그인한 회원 엔티티 조회

        Cart cart = cartRepository.findByMemberId(member.getId());// 현재 로그인한 회원의 장바구니 엔티티 조회

        if(cart == null){
            cart = Cart.createCart(member); // 상품을 처음으로 장바구니에 담을 경우 해당 회원의 장바구니 엔티티 생성
            cartRepository.save(cart);
        }

        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());// 현재 상품이 장바구니에 이미 들어가 있는지 조회

        if(savedCartItem != null){
            savedCartItem.addCount(cartItemDto.getCount()); // 장바구니에 이미 있던 상품일 경우 기준 수량에 현재 장바구니에 담을 수량 만큼 더해줌

            return savedCartItem.getId();
        }else {
            // 장바구니, 상품 엔티티, 장바구니에 담을 수량을 이용해 CartItem 엔티티 생성
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(cartItem); // 장바구니에 들어갈 상품을 저장

            return cartItem.getId();
        }
    }


    /**
     * 장바구니 상품 조회
     * @param email
     * @return
     */
    //현재 로그인한 회원의 정보를 이용하여 장바구니에 들어있는 상품을 조회
    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email){

        List<CartDetailDto> cartDetailDtoList =  Collections.emptyList();

        Member member = memberRepository.findByEmail(email);

        Cart cart = cartRepository.findByMemberId(member.getId());

        if(cart == null){
            return cartDetailDtoList;
        }

        cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId()); //

        return cartDetailDtoList;

    }

    /**
     * 장바구니 상품을 저장한 회원이 같은지 검사하는 메서드
     * @param cartItemId
     * @param email
     * @return
     */
    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String email) {

        Member curMember = memberRepository.findByEmail(email); // 현재 로그인한 회원을 조회
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

        Member savedMember = cartItem.getCart().getMember(); // 장바구니 상품을 저장한 회원을 조회

        if (!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) { // 현재 로그인한 회원과 장바구니 상품을 저장항 회원이 다를경우 false, 같을경우 true
            return false;
        }

        return true;
    }

    /**
     * 장바구니 상품 수량을 업데이트 메서드
     * @param cartItemId
     * @param count
     */
    @Transactional
    public void updateCartItemCount(Long cartItemId, int count){

        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

        cartItem.updateCount(count);
    }

    /**
     * 장바구니 상품 삭제하기
     * @param cartItemId
     */
    @Transactional
    public void deleteCartItem(Long cartItemId){ //장바구니 상품 삭제하기

        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }

    //주문 로직으로 전달할 orderDto 리스트 생성 및 주문 로직 호출, 주문한 상품은 장바구니에서 제거하는 로직
    @Transactional
    public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String email){

        Long orderId = create(cartOrderDtoList, email);

        deleteCartItem(cartOrderDtoList);

        return orderId;
    }

    private void deleteCartItem(List<CartOrderDto> cartOrderDtoList) {
        for(CartOrderDto cartOrderDto : cartOrderDtoList){ // 주문한 상품들을 장바구니에서 제거

            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);
        }
    }

    private Long create(List<CartOrderDto> cartOrderDtoList, String email) {
        List<OrderDto> orderDtoList = new ArrayList<>();
        for (CartOrderDto cartOrderDto : cartOrderDtoList){

            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);
            orderDtoList.add(OrderDto.from(cartItem));
        }

        return orderService.orders(orderDtoList, email); // 장바구니에 담은 상품을 주문하도록 주문 로직을 호출
    }
}

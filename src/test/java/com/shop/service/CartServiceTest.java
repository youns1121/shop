package com.shop.service;

import com.shop.domain.Cart;
import com.shop.domain.CartItem;
import com.shop.domain.Item;
import com.shop.domain.Member;
import com.shop.dto.CartItemDto;
import com.shop.enums.ItemSellStatus;
import com.shop.repository.CartItemRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CartServiceTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CartService cartService;

    @Autowired
    CartItemRepository cartItemRepository;

    public Item saveItem(){

        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);

        return itemRepository.save(item);

    }

    public Member saveMember(){
        Member member = new Member();

        member.setEmail("test@test.com");

        return memberRepository.save(member);
    }


    @Test
    @DisplayName("장바구니 담기 테스트")
    public void addCart(){

        Item item = saveItem();
        Member member = saveMember();

        CartItemDto cartItemDto = new CartItemDto();

        cartItemDto.setCount(5); // 장바구니에 담을 수량,
        cartItemDto.setItemId(item.getId()); // 상품 아이디

        Long cartItemId = cartService.addCart(cartItemDto, member.getEmail()); // 상품을 장바구니에 담는 로직 호출 결과를 cartItemId 변수에 저장

        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new); // 장바구니 상품 아이디를 이용하여 생성된 장바구니 상품 정보를 조회

        assertEquals(item.getId(), cartItem.getItem().getId()); // 상품의 아이디랑, 카트에 있는 상품의 아이디랑 같냐
        assertEquals(cartItemDto.getCount(), cartItem.getCount()); // 장바구니에 담았던 수량이랑 실제 장바구니에 담았던 수량이랑 같냐


    }





}
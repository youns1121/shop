package com.shop.service;

import com.shop.dto.OrderDto;
import com.shop.domain.Item;
import com.shop.domain.Member;
import com.shop.domain.Order;
import com.shop.domain.OrderItem;
import com.shop.enums.ItemSellStatus;
import com.shop.enums.OrderStatus;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MemberRepository memberRepository;


    public Item saveItem(){ // 테스트를 위한 주문할 상품
        Item item = new Item();

        item.setItemName("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        return itemRepository.save(item);
    }

    public Member saveMember(){ // 테스트를 위한 회원 정보

        Member member = new Member();
        member.setEmail("test@test.com");

        return memberRepository.save(member);
    }

    @Test
    @DisplayName("주문 테스트")
    public void Order(){

        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setCount(1); // 한 개 주문
        orderDto.setItemId(item.getId());

        Long orderId = orderService.order(orderDto, member.getEmail()); // 주문 로직 호출 결과 생성된 주문 번호를 orderID 변수에 저장

        Order order = orderRepository.findById(orderId) // 주문 번호를 이용해서 저장된 주문 정보를 조회
                .orElseThrow(EntityExistsException::new);

        List<OrderItem> orderItemList = order.getOrderItemList();

        int totalPrice = orderDto.getCount() * item.getPrice();// 주문상품의 총 가격

        assertEquals(totalPrice, order.getTotalPrice()); // 주문한 상품의 총 가격과 데이터베이스에 저장된 상품의 가격을 비교해서 같으면 테스트 성공


    }

    @Test
    @DisplayName("주문 취소 테스트")
    public void cancelOrder(){

        Item item = saveItem(); // 상품 데이터 생성, 상품 재고는 100개
        Member member = saveMember(); //회원 데이터 생성

        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());

        Long orderId = orderService.order(orderDto, member.getEmail());// 주문데이터 생성, 주문 개수는 총 10개

        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityExistsException::new); // 생성한 주문 엔티티를 조회

        orderService.cancelOrder(orderId); //해당 주문을 취소

        assertEquals(OrderStatus.CANCEL, order.getOrderStatus()); // 주문의 상태가 취소 상태라면 테스트 통과
        assertEquals(100, item.getStockNumber()); // 취소 후 상품의 재고가 처음 재고 개수인 100개와 동일 하면 테스트 통과



    }



}
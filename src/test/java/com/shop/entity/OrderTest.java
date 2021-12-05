package com.shop.entity;

import com.shop.enums.ItemSellStatus;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    public Item createItem(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("상세설명");
        item.setItemSellStatus(ItemSellStatus.SELL); // 제품 판매상태
        item.setStockNumber(100); // 재고수량
        item.setDelYn("N");
        item.setCreateTime(LocalDateTime.now()); //등록시간
        item.setUpdateTime(LocalDateTime.now()); //수정시간

        return item;
    }

    public Order createOrder(){
        Order order = new Order();

        for(int i = 0; i<3; i++){
            Item item = this.createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10); //수량
            orderItem.setOrderPrice(1000); //주문가격
            orderItem.setOrder(order);
            orderItem.setDelYn("N");
            order.getOrderItemList().add(orderItem);
        }
        Member member = new Member();
        memberRepository.save(member);

        order.setMember(member);
        orderRepository.save(order);
        return order;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest(){

        Order order = new Order();

        for (int i = 0; i<3; i++) {
            Item item = this.createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item); // orderItem item 객체 set
            orderItem.setCount(10); // 수량
            orderItem.setOrderPrice(1000); //주문가격
            orderItem.setDelYn("N"); //
            orderItem.setOrder(order);
            order.getOrderItemList().add(orderItem); // 영속성 컨텍스트에 저장되지 않은 orderITem 엔티티를 order 엔티티에 담아줌
        }

        orderRepository.saveAndFlush(order); // order 엔티티를 저장하면서 강제로 flush를 호출하여 영속성 컨텍스트에 있는 객체들을 DB에 반영
        em.clear(); // 영속성 컨텍스트의 상태를 초기화

        // 영속성 컨텍스트를 초기화했기 때문에 DB에서 주문 엔티티를 조회. select 쿼리문이 실행
        Order saveOrder = orderRepository.findById(order.getId()).orElseThrow(EntityNotFoundException::new);
        assertEquals(3, saveOrder.getOrderItemList().size()); // 3이 orderitemlist 사이즈와 일치 여부

    }

    @Test
    @DisplayName("고아객체 제거 테스트")
    public void orphanRemovalTest(){
        Order order = this.createOrder();
        order.getOrderItemList().remove(0); // order 엔티티에서 관리하고 있는 orderItem 리스트의 0번째 인덱스 요소를 제거
        em.flush();
    }





}
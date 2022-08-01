package com.shop.service;

import com.shop.domain.*;
import com.shop.dto.OrderDto;
import com.shop.dto.OrderHisDto;
import com.shop.dto.OrderItemDto;
import com.shop.dto.response.OrderDetailResponseDto;
import com.shop.enums.StatusEnum;
import com.shop.global.error.exception.EmailNotFoundException;
import com.shop.global.error.exception.ErrorCode;
import com.shop.global.error.exception.ItemNotFoundException;
import com.shop.global.error.exception.OrderNotFoundException;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderRepository;
import com.shop.repository.custom.OrderRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;
    private final OrderRepositoryCustom orderRepositoryCustom;

    @Transactional
    public Long order(OrderDto orderDto, String email){

        OrderItem orderItem = OrderItem.create(getItem(orderDto), orderDto.getCount()); // 주문할 상품 엔티티와 주문 수량을 이용하여 주문 상품 엔티티를 생성

        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(orderItem);

        Order order = Order.create(getMember(email), orderItemList);// 회원 정보와 주문할 상품 리스트 정보를 이용하여 주문 엔티티를 생성
        orderItem.setOrder(order);
        orderRepository.save(order);

        return order.getId();
    }

    @Transactional(readOnly = true)
    public OrderDetailResponseDto getOrderDetail(String orderId){

        return orderRepositoryCustom.getOrderDetail(orderId);
    }


    //주문목록 조회 로직
    @Transactional(readOnly = true)
    public Page<OrderHisDto> getOrderList(String email, Pageable pageable){

        List<Order> orderList = orderRepository.findOrders(email, pageable); //유저 아이디와 페이징 조건을 이용하여 주문 목록을 조회
        Long totalCount = orderRepository.countOrder(email); // 유저의 주문 총 개수를 구함

        List<OrderHisDto> orderHisDtoList = new ArrayList<>();

        for(Order order : orderList){ // 주문 리스트를 순회 하면서 구매 이력 페이지에 전달할 Dto 생성

            OrderHisDto orderHisDto = new OrderHisDto(order);
            List<OrderItem> orderItemList = order.getOrderItemList();
            addOrderItem(orderHisDto, orderItemList);
            orderHisDtoList.add(orderHisDto);
        }
        return new PageImpl<>(orderHisDtoList, pageable, totalCount); // 페이지 구현 객체를 생성하여 반환

    }


    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email){ //현재 로그인한 사용자와 주문 데이터를 생성한 사용자가 같은지 검사

        Member curMember = getMember(email);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityExistsException::new);
        Member savedMember = order.getMember();

        return StringUtils.equals(curMember.getEmail(), savedMember.getEmail());
    }

    /**
     * 주문 취소
     * @param orderId
     */
    @Transactional
    public void cancelOrder(Long orderId){

        Order order = getOrder(orderId);

        order.cancelOrder(); // 주문 취소 상태로 변경하면 변경 감지 기능에 의해서 트랜잭션이 끝날 때 update 쿼리가 실행됨
    }


    // 장바구니에서 주문 생성
    @Transactional(readOnly = true)
    public Long orders(List<OrderDto> orderDtoList, String email){

        List<OrderItem> orderItemList = new ArrayList<>();

        addOrderItemList(orderDtoList, orderItemList);

        Order order = Order.create(getMember(email), orderItemList); // 현재 로그인한 회원과 주문 상품 목록을 이용하여 주문 엔티티를 만들어줌

        orderRepository.save(order); // 주문 데이터 저장

        return order.getId();
    }



    private Item getItem(OrderDto orderDto) {

        return itemRepository.findById(orderDto.getItemId())
                .orElseThrow(() -> new ItemNotFoundException(ErrorCode.ITEM_NOT_FOUND.getMessage()));
    }

    private Member getMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException(ErrorCode.EMAIL_NOT_FOUND.getMessage()));
    }

    private void addOrderItem(OrderHisDto orderHisDto, List<OrderItem> orderItemList) {
        for(OrderItem orderItem : orderItemList){
            ItemImg itemImg = itemImgRepository.findByItemIdAndRepImgYn(orderItem.getItem().getId(), StatusEnum.FLAG_Y.getStatusMessage()); // 주문한 상품의 대표 이미지를 조회
            OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
            orderHisDto.addOrderItemDto(orderItemDto);
        }
    }

    private void addOrderItemList(List<OrderDto> orderDtoList, List<OrderItem> orderItemList) {
        for(OrderDto orderDto : orderDtoList){ // 주문할 상품 리스트를 만들어줌

            orderItemList.add(OrderItem.create(getItem(orderDto), orderDto.getCount()));
        }
    }

    private Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(ErrorCode.ORDER_NOT_FOUND.getMessage()));
    }
}

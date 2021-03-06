package com.shop.repository;

import com.shop.domain.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o " +
            "where o.member.email = :email " +
            "order by o.orderDate desc"
      )
    List<Order> findOrders(@Param("email") String email, Pageable pageable); // 상품의 대표 이미지를 찾는 쿼리메서드, 페이지에서 주문 상품의 대표 이미지를 보여주기 위함

    @Query("select count(o) from Order o " +
            "where o.member.email = :email"
     )
    Long countOrder(@Param("email") String email); //현재 로그인한 회원의 주문 개수



}

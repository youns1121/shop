package com.shop.repository;

import com.shop.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByMemberId(Long memberId); // 현재 로그인한 Cart 엔티티 찾기
}

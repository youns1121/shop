package com.shop.repository;

import com.shop.domain.CartItem;
import com.shop.dto.CartDetailDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    //카트 아이디와 상품 아이디를 이용해서 상품이 장바구니에 들어가있는지 조회
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    @Query("select new com.shop.dto.CartDetailDto(ci.id, i.itemName, i.price, ci.count, im.imgUrl) " + // DTO 클래스 생성자에 명시한 순
        "from CartItem ci, ItemImg im " +
        "join ci.item i " +
        "where ci.cart.id = :cartId " +
        "and im.item.id = ci.item.id " + // 장바구니에 담겨있는 상품의 대표 이미지만 가지고 오도록 조건문 작성
        "and im.repImgYn = 'Y' " + // 장바구니에 담겨있는 상품의 대표 이미지만 가지고 오도록 조건문 작성
        "order by ci.createTime desc")
    List<CartDetailDto> findCartDetailDtoList(@Param("cartId") Long cartId);

}

package com.shop.repository;

import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>{

    List<Item> findByItemNm(String itemNm); // 아이템 이름을 찾아서 Item 객체를 반환하고 싶다

    // 상품을 상품명과 상세 설명을 or 조건을 이용해아 조회하는 쿼리 메서드
    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

    List<Item> findByPriceLessThan(Integer price); //price 변수보다 값이 작은 상품을 조회

    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price); //price 변수보다 값이 작은 상품을 조회하고 가격순으로 내림차순


}

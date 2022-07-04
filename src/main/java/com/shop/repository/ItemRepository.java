package com.shop.repository;

import com.shop.domain.Item;
import com.shop.repository.custom.ItemRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {

    List<Item> findByItemName(String itemName); // 아이템 이름을 찾아서 Item 객체를 반환하고 싶다

    // 상품을 상품명과 상세 설명을 or 조건을 이용해아 조회하는 쿼리 메서드
    List<Item> findByItemNameOrItemDetail(String itemName, String itemDetail);

    List<Item> findByPriceLessThan(Integer price); //price 변수보다 값이 작은 상품을 조회

    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price); //price 변수보다 값이 작은 상품을 조회하고 가격순으로 내림차순


    //itemDetail이 포함되며 가격순으로 내림차순 Item 조회
   @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail); //@Param을 활용하여 명시*/

    //기존 데이터베이스에서 사용하던 쿼리를 그대로 사용 - native query
    @Query(value="select * from item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery = true)
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail); //@Param을 활용하여 명시*/


}

package com.shop;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.domain.Item;

import com.shop.domain.QItem;
import com.shop.enums.ItemSellStatus;
import com.shop.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest
//@TestPropertySource(locations = "classpath:application.yml")
class ShopApplicationTests {

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em; // 영속성 컨텍스트를 사용하기 위해, EntityManager 빈을 주입

    @Test
    @DisplayName("상품저장테스트")
    void createItemTest() {
        Item item = new Item();
        item.setItemName("테스트상품");
        item.setPrice(5000);
        item.setItemDetail("테스트상세설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);

        Item saveItem = itemRepository.save(item);

        System.out.println(saveItem.toString());
    }

    @Test
    @DisplayName("상품 리스트저장 테스트")
    void createItemListTest() {
        for(int i=0; i<=10; i++) {
            Item item = new Item();
            item.setItemName("테스트상품");
            item.setPrice(5000+i);
            item.setItemDetail("테스트상세설명");
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100+i);

            Item saveItem = itemRepository.save(item);

            System.out.println(saveItem.toString());
        }
    }


    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemName(){
        this.createItemListTest();
        List<Item> itemList = itemRepository.findByItemName("테스트상품1");
        for (Item item: itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    public void findByItemNameOrItemDetail() {
        this.createItemListTest(); //상품을 만드는 테스트 메서드
        List<Item> itemList =
                itemRepository.findByItemNameOrItemDetail("테스트 상품1", "테스트 상품 상세 설명5");
            for (Item item : itemList) {
                System.out.println(item.toString());
            }

    }

    @Test
    @DisplayName("가격 LessThen 테스트")
    public void findByPriceLessThanOrderByPriceDescTest(){
        this.createItemListTest(); // 상품생성 테스트 메서드
        List<Item> itemList = itemRepository.findByPriceLessThan(10005);
        for (Item item: itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 LessThen 테스트")
    public void findByPriceLessThanTest(){
        this.createItemListTest(); // 상품생성 테스트 메서드
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        for (Item item: itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void findByItemDetailTest(){
        this.createItemListTest();
        List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");
        for (Item item: itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("@Query를 이용한 Native query 상품 조회 테스트")
    public void findByItemDetailByNativeTest(){
        this.createItemListTest();
        List<Item> itemList = itemRepository.findByItemDetailByNative("테스트 상품 상세 설명");
        for (Item item: itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("Querydsl 조회테스트1")
    public void queryDslTest(){
        this.createItemListTest();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qitem = QItem.item; // Querydsl을 통해 쿼리르 생성하기 위해 플러그인을 통해 자동으로 생성된 QItem 객체를 이용
        JPAQuery<Item> query = queryFactory.selectFrom(qitem)
                .where(qitem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qitem.itemDetail.like("%"+"테스트 상품 상세 설명"+"%"))
                .orderBy(qitem.price.desc());

        List<Item> itemList = query.fetch(); // 조회결과 리스트 반환, fetch() 메서드 실행 시점에서 쿼리문이 실행

        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }








}

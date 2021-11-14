package com.shop;

import com.shop.entity.Item;
import com.shop.enums.ItemSellStatus;
import com.shop.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
//@TestPropertySource(locations = "classpath:application.yml")
class ShopApplicationTests {

    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("상품저장테스트")
    void createItemTest() {
        Item item = new Item();
        item.setItemNm("테스트상품");
        item.setPrice(5000);
        item.setItemDetail("테스트상세설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setCreateTime(LocalDateTime.now());
        item.setModifiTime(LocalDateTime.now());
        item.setDelYn("N");
        Item saveItem = itemRepository.save(item);

        System.out.println(saveItem.toString());
    }

    @Test
    @DisplayName("상품 리스트저장 테스트")
    void createItemListTest() {
        for(int i=0; i<=10; i++) {
            Item item = new Item();
            item.setItemNm("테스트상품");
            item.setPrice(5000);
            item.setItemDetail("테스트상세설명");
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setCreateTime(LocalDateTime.now());
            item.setModifiTime(LocalDateTime.now());
            item.setDelYn("N");
            Item saveItem = itemRepository.save(item);

            System.out.println(saveItem.toString());
        }
    }


    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNm(){
        this.createItemListTest();
        List<Item> itemList = itemRepository.findByItemNm("테스트상품1");
        for (Item item: itemList) {
            System.out.println(item.toString());
        }

    }

}

package com.shop.domain;


import com.shop.domain.comm.BaseEntity;
import com.shop.dto.form.ItemFormDto;
import com.shop.enums.ItemSellStatus;
import com.shop.global.error.exception.ErrorCode;
import com.shop.global.error.exception.OutOfStockException;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@EqualsAndHashCode(of="id", callSuper = false)
@ToString
@Getter
@Table(name="item")
@Entity
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id")
    private Long id;

    @Column(name ="item_name", nullable = false)
    private String itemName;

    @Column(name="price", nullable = false)
    private int price;

    @Column(name="stock_number", nullable = false)
    private int stockNumber;

    @Column(name="item_detail", nullable = false)
    private String itemDetail; //상품 상세 설명

    @Enumerated(EnumType.STRING)
    @Column(name = "item_sell_status")
    private ItemSellStatus itemSellStatus; //상품 판매상태

    @Builder
    public Item(Long id, String itemName, int price, int stockNumber, String itemDetail, ItemSellStatus itemSellStatus) {
        this.id = id;
        this.itemName = itemName;
        this.price = price;
        this.stockNumber = stockNumber;
        this.itemDetail = itemDetail;
        this.itemSellStatus = itemSellStatus;
    }

    public void updateItem(ItemFormDto itemFormDto){

        this.itemName = itemFormDto.getItemName();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    public void removeStock(int stockNumber){
        int restStock = this.stockNumber - stockNumber; // 상품의 재고 수량에서 주문 후 남은 재고 수량을 구함함
        if(restStock < 0){
            throw new OutOfStockException(ErrorCode.OUT_OF_STOCK.getMessage()); // 상품의 재고가 주문 수량보다 작을 경우 재고 부족으로 예외를 발생
        }
        this.stockNumber = restStock; // 주문 후 남은 재고 수량을 상품의 현재 재고 값으로 할당;
   }

    public void addStock(int stockNumber){  // 상품의 재고를 더해주는 메소드

        this.stockNumber += stockNumber;
    }
}

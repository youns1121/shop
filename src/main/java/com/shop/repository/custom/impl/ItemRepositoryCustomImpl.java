package com.shop.repository.custom.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import com.shop.domain.QItem;
import com.shop.domain.QItemImg;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.domain.Item;

import com.shop.dto.QMainItemDto;
import com.shop.enums.ItemSellStatus;
import com.shop.enums.StatusEnum;
import com.shop.repository.custom.ItemRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private JPAQueryFactory queryFactory; //동적으로 쿼리를 생성하기 위해

    public ItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em); // JPAQueryFactory 생성자로 em 객체를 넣어줌
    }

    private BooleanExpression searchSellStatuEq(ItemSellStatus searchSellStatus){
        return searchSellStatus == null ? null: QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    private BooleanExpression regDtsAfter(String searchDateType){ // 해당 시간 이후로 등록된 상품만 조회

        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all", searchDateType) || searchDateType == null){ // 둘중 하나라도 null 이면
            return null;
        } else if(StringUtils.equals("1d", searchDateType)){
            dateTime = dateTime.minusDays(1);
        } else if(StringUtils.equals("1w", searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        } else if(StringUtils.equals("1m", searchDateType)){
            dateTime = dateTime.minusMonths(1);
        } else if(StringUtils.equals("6m", searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }

            return QItem.item.createTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){ // searchBy의 값에 따라서 상품명에 검색어를 포함하고 있는 상품 또는 상품 생성자의 아이디에 검색어를 포함하고 있는 상품을 조회하도록 조건값을 반환

        if(StringUtils.equals("itemName", searchBy)){
            return QItem.item.itemName.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("createdBy", searchBy)){
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }

    private BooleanExpression itemNameLike(String searchQuery){ // 검색어가 null이 아니라면 상품명에 해당 검색어가 포함되는 상품을 조회하는 조건을 반환

        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemName.like("%"+searchQuery+"%");
   }



    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {

        QueryResults<Item> results = queryFactory //쿼리 생성
                .selectFrom(QItem.item) // 상품 데이터 조회하기 위해서 Qitem의 item 지정
                .where(regDtsAfter(itemSearchDto.getSearchDateType()), // where 조건절 ',' 단위를 넣어줄경우 and 조건으로 인식
                        searchSellStatuEq(itemSearchDto.getItemSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))

                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset()) // 데이터를 가지고올 인덱스를 지정
                .limit(pageable.getPageSize()) // 한번에 가지고 올 최대 갯수 지정
                .fetchResults(); // QueryResults를 반환, 상품 데이터 리스트 조회 및 상품 데이터 전체 개수를 조회하는 2번의 쿼리문 실행
        List<Item> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total); // 조회한 데이터를 Page 클래스의 구현체인 PageImpl 객체로 반환
    }

    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {

        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        QueryResults<MainItemDto> results = queryFactory
                .select(
                        new QMainItemDto( // QMainItemDto의 생성자에 반환할 값들을 넣어줌, @QueryProject을 사용하면 DTO로 바로 조회가 가능
                                item.id,
                                item.itemName,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price
                        )

                )
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repImgYn.eq(StatusEnum.FLAG_Y.Value())) //대표상품 이미지 여부
                .where(itemNameLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }
}

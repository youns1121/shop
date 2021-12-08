package com.shop.entity;

import com.shop.entity.comm.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Table(name="item_img")
@Entity
public class ItemImg extends BaseEntity { //상품이미지

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "img_name")
    private String imgName; //이미지 파일명

    @Column(name ="ori_img_name")
    private String oriImgName; //원본 이미지 파일명

    @Column(name = "img_url")
    private String imgUrl; //이미지 조회 경로

    @Column(name = "rep_img_yn")
    private String repImgYn; // 대표 이미지 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public void updateItemimg(String oriImgName, String imgName, String imgUrl){ //이미지 정보 업데이트 메서드
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }


}

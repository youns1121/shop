package com.shop.domain;

import com.shop.domain.comm.BaseEntity;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@EqualsAndHashCode(of="id", callSuper = false)
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


    @Builder
    public ItemImg(Long id, String imgName, String oriImgName, String imgUrl, String repImgYn, Item item) {
        this.id = id;
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
        this.repImgYn = repImgYn;
        this.item = item;
    }

    public void updateItemImg(String oriImgName, String imgName, String imgUrl){ //이미지 정보 업데이트 메서드
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }

    public void setItem(Item item){

        this.item = item;
    }

    public void updateRepImgYn(String repImgYn){
        this.repImgYn = repImgYn;
    }


}

package com.shop.domain;

import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@EqualsAndHashCode(of = "boardImgId")
@Getter
@NoArgsConstructor
@Table(name = "board_img")
@Entity
public class BoardImg {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "board_img_id", nullable = false)
    private Long boardImgId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;
}

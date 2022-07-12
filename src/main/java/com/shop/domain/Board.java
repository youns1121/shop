package com.shop.domain;

import com.shop.domain.comm.BaseEntity;
import com.shop.dto.BoardCreateDto;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(of = "boardId")
@ToString(exclude = "boardImgList" )
@Getter
@NoArgsConstructor
@Entity
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "board_id", nullable = false)
    private Long boardId;

    @Column(name = "board_title", nullable = false)
    private String boardTitle;

    @Column(name = "board_contents", nullable = false)
    private String boardContents;

    @Column(name = "del_yn")
    private String delYn;

    @OneToMany(mappedBy = "board")
    @ToString.Exclude
    private List<BoardImg> boardImgList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @ToString.Exclude
    private Member member;


    @Builder
    public Board(Long boardId, String boardTitle, String boardContents, String delYn, List<BoardImg> boardImgList, Member member) {
        this.boardId = boardId;
        this.boardTitle = boardTitle;
        this.boardContents = boardContents;
        this.delYn = delYn;
        this.boardImgList = boardImgList;
        this.member = member;
    }

        public static Board create(BoardCreateDto boardCreateDto){


        return Board.builder()
                .boardTitle(boardCreateDto.getBoardTitle())
                .boardContents(boardCreateDto.getBoardContents())
                .delYn(boardCreateDto.getDelYn())
                .build();
    }
}

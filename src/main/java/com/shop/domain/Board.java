package com.shop.domain;

import com.shop.domain.comm.BaseEntity;
import com.shop.dto.BoardUpdateDto;
import com.shop.dto.form.BoardFormDto;
import com.shop.enums.StatusEnum;
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
    private List<BoardFile> boardFileList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    @Builder
    public Board(Long boardId, String boardTitle, String boardContents, String delYn, List<BoardFile> boardFileList, Member member) {
        this.boardId = boardId;
        this.boardTitle = boardTitle;
        this.boardContents = boardContents;
        this.delYn = delYn;
        this.boardFileList = boardFileList;
        this.member = member;
    }



    public static Board create(BoardFormDto boardFormDto){

        return Board.builder()
                .boardTitle(boardFormDto.getBoardTitle())
                .boardContents(boardFormDto.getBoardContents())
                .member(boardFormDto.getMember())
                .delYn(StatusEnum.FLAG_N.getStatusMessage())
                .build();
    }

    public void update(BoardUpdateDto boardUpdateDto){

        this.boardTitle = boardUpdateDto.getBoardTitle();
        this.boardContents = boardUpdateDto.getBoardContents();
    }

    public void delete(Board board){

        board.delYn = StatusEnum.FLAG_Y.getValue();
    }
}

package com.shop.domain;

import com.shop.domain.comm.BaseEntity;
import com.shop.dto.BoardFileDto;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(of = "boardFileId")
@Getter
@NoArgsConstructor
@Table(name = "board_file")
@Entity
public class BoardFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "board_file_id", nullable = false)
    private Long boardFileId;

    @Column(name = "board_file_name", nullable = false)
    private String boardFileName;

    @Column(name = "board_file_size", nullable = false)
    private long boardFileSize;

    @Column(name = "board_file_path", nullable = false)
    private String boardFilePath;

    @Column(name = "board_file_store_name", nullable = false)
    private String boardFileStoreName;

    @Column(name = "board_file_extension", nullable = false)
    private String boardFileExtension;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;


    @Builder
    public BoardFile(Long boardFileId, String boardFileName, long boardFileSize, String boardFilePath, String boardFileStoreName, String boardFileExtension, Board board) {
        this.boardFileId = boardFileId;
        this.boardFileName = boardFileName;
        this.boardFileSize = boardFileSize;
        this.boardFilePath = boardFilePath;
        this.boardFileStoreName = boardFileStoreName;
        this.boardFileExtension = boardFileExtension;
        this.board = board;
    }

    public static BoardFile from(BoardFileDto boardFileDto){

        return BoardFile.builder()
                .boardFileName(boardFileDto.getBoardFileName())
                .boardFileSize(boardFileDto.getBoardFileSize())
                .boardFilePath(boardFileDto.getBoardFilePath())
                .boardFileStoreName(boardFileDto.getBoardFileStoreName())
                .boardFileExtension(boardFileDto.getBoardFileExtension())
                .board(boardFileDto.getBoard())
                .build();
    }
}

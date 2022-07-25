package com.shop.repository.custom;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.domain.QBoard;
import com.shop.domain.QBoardFile;
import com.shop.domain.QMember;
import com.shop.dto.request.BoardRequestDto;
import com.shop.dto.response.BoardResponsePagingDto;
import com.shop.enums.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QBoard board = QBoard.board;
    QBoardFile boardFile = QBoardFile.boardFile;
    QMember member = QMember.member;

    public Page<BoardResponsePagingDto> getSearchBoardPage(BoardRequestDto request, Pageable pageable){

        List<BoardResponsePagingDto> contents = queryFactory
                .select(Projections.fields(
                        BoardResponsePagingDto.class
                        , board.boardId.as("boardId")
                        , board.boardTitle.as("boardTitle")
                        , board.boardContents.as("boardContents")
                        , board.createTime.as("createTime")
                        , board.updateTime.as("updateTime")
                        , board.delYn.as("delYn")
                        , new CaseBuilder()
                                .when(boardFile.board.boardId.isNotNull())
                                .then(StatusEnum.FLAG_Y.getValue())
                                .otherwise(StatusEnum.FLAG_N.getValue())
                                .as("boardFileYn")
                        , member.email.as("memberEmail")
                        , member.name.as("memberName")
                ))
                .from(board)
                .innerJoin(board.member, member)
                .leftJoin(board.boardFileList, boardFile)

                .where(board.delYn.eq(StatusEnum.FLAG_N.getValue()))
                .groupBy(board.boardId)

                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        long total = queryFactory
                .select(board)
                .from(board)
                .leftJoin(board.boardFileList, boardFile)
                .where(board.delYn.eq(StatusEnum.FLAG_N.getValue()))
                .groupBy(board)
                .fetchCount();

        return new PageImpl<>(contents, pageable, total);
    }
}

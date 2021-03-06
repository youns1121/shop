package com.shop.service;

import com.shop.domain.Board;
import com.shop.domain.BoardFile;
import com.shop.domain.Member;
import com.shop.dto.BoardFileDto;
import com.shop.dto.BoardUpdateDto;
import com.shop.dto.form.BoardFormDto;
import com.shop.dto.request.BoardRequestDto;
import com.shop.dto.response.BoardResponseDto;
import com.shop.dto.response.BoardResponsePagingDto;
import com.shop.enums.StatusEnum;
import com.shop.global.error.exception.BoardNotFoundException;
import com.shop.global.error.exception.ErrorCode;
import com.shop.repository.BoardFileRepository;
import com.shop.repository.BoardRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.custom.BoardRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    private final MemberRepository memberRepository;
    private final BoardRepositoryCustom boardRepositoryCustom;

    @Transactional
    public Board createBoard(BoardFormDto boardFormDto, String memberName){


        Member findMember = memberRepository.findByEmail(memberName)
                .orElseThrow(() -> new IllegalArgumentException("로그인을 해주세요"));

        boardFormDto.setMember(findMember);

        return boardRepository.save(Board.create(boardFormDto));
    }

    @Transactional
    public Board updateBoard(BoardUpdateDto boardUpdateDto, Long id){

        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND.getMessage()));

        board.update(boardUpdateDto);

        return board;
    }

    @Transactional(readOnly = true)
    public BoardResponseDto getBoardDetail(Long boardId){

        Board board = boardRepository.findByBoardIdAndDelYn((boardId), StatusEnum.FLAG_N.getStatusMessage())
                .orElseThrow(() -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND.getMessage()));

        List<BoardFileDto> boardFileDtoList = Collections.emptyList();

        if(!CollectionUtils.isEmpty(board.getBoardFileList())){

            boardFileDtoList = getBoardFileDtoList(board);
        }

        return BoardResponseDto.of(board, boardFileDtoList);
    }

    public Page<BoardResponsePagingDto> getBoardSearchList(BoardRequestDto boardRequestDto, Pageable pageable){

        return boardRepositoryCustom.getSearchBoardPage(boardRequestDto, pageable);
    }

    private List<BoardFileDto> getBoardFileDtoList(Board board) {

        List<BoardFileDto> boardFileDtoList= new ArrayList<>();

        for(BoardFile boardFile : board.getBoardFileList()){

            boardFileDtoList.add(BoardFileDto.from(boardFile));
        }

        return boardFileDtoList;
    }

    @Transactional
    public void deleteBoard(Long id){

        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시판입니다."));

        if(!CollectionUtils.isEmpty(board.getBoardFileList())){
            deleteFile(board);
        }
        board.delete(board);
    }

    private void deleteFile(Board board) {
        for(BoardFile boardFile : board.getBoardFileList()){

            BoardFile file = boardFileRepository.findById(boardFile.getBoardFileId())
                    .orElseThrow(() -> new EntityNotFoundException("파일이 존재하지 않습니다"));

            boardFileRepository.delete(file);
        }
    }
}

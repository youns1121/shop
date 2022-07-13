package com.shop.service;

import com.shop.domain.Board;
import com.shop.domain.BoardFile;
import com.shop.domain.Member;
import com.shop.dto.BoardFileDto;
import com.shop.dto.BoardUpdateDto;
import com.shop.dto.form.BoardFormDto;
import com.shop.dto.response.BoardResponseDto;
import com.shop.enums.StatusEnum;
import com.shop.repository.BoardRepository;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;


    @Transactional
    public Board createBoard(BoardFormDto boardFormDto, String memberName){


        Member findMember = memberRepository.findByEmail(memberName)
                .orElseThrow(() -> new IllegalArgumentException("로그인을 해주세요"));

        boardFormDto.setMember(findMember);

        return boardRepository.save(Board.create(boardFormDto));
    }

    @Transactional
    public void updateBoard(BoardUpdateDto boardUpdateDto){

        Board board = boardRepository.findById(boardUpdateDto.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다"));

        board.update(boardUpdateDto);
    }

    public void memberBoardCheck(Long memberId, Long boardMemberId){

        if(!memberId.equals(boardMemberId)){
            throw new EntityNotFoundException("잘못된 접근입니다.");
        }

    }


    @Transactional(readOnly = true)
    public BoardResponseDto getBoardDetail(String boardId){

        Board board = boardRepository.findByBoardIdAndDelYn(Long.valueOf(boardId), StatusEnum.FLAG_N.getStatusMessage())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        List<BoardFileDto> boardFileDtoList = Collections.emptyList();

        if(!CollectionUtils.isEmpty(board.getBoardFileList())){

            boardFileDtoList = getBoardFileDtoList(board);
        }


        return BoardResponseDto.of(board, boardFileDtoList);
    }

    private List<BoardFileDto> getBoardFileDtoList(Board board) {

        List<BoardFileDto> boardFileDtoList= new ArrayList<>();

        for(BoardFile boardFile : board.getBoardFileList()){

            boardFileDtoList.add(BoardFileDto.from(boardFile));
        }

        return boardFileDtoList;
    }
}

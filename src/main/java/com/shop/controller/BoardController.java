package com.shop.controller;

import com.shop.domain.Board;
import com.shop.domain.Member;
import com.shop.dto.BoardFileDownloadDto;
import com.shop.dto.BoardUpdateDto;
import com.shop.dto.form.BoardFormDto;
import com.shop.dto.request.BoardRequestDto;
import com.shop.dto.response.BoardResponseDto;
import com.shop.service.BoardFileService;
import com.shop.service.BoardService;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.Principal;
import java.util.List;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
@RequestMapping("/board")
@Controller
public class BoardController {

    private final BoardService boardService;
    private final BoardFileService boardFileService;
    private final MemberService memberService;

    @GetMapping("/list")
    public String listBoard(Model model, BoardRequestDto boardRequestDto, Pageable pageable){

        model.addAttribute("boardList", boardService.getBoardSearchList(boardRequestDto, pageable));

        return "board/boardList";
    }

    @GetMapping("/new")
    public String createBoard(Model model, Principal principal){

        Member member = memberService.getMember(principal);

        model.addAttribute("member", member.getName());

        model.addAttribute("board", new BoardFormDto());

        return "board/boardForm";
    }

    @PostMapping("/new")
    public String createBoard(@Validated @ModelAttribute("board") BoardFormDto boardFormDto,  BindingResult bindingResult,
                              Model model, List<MultipartFile> boardFileList, Principal principal) throws IOException {

        if (bindingResult.hasErrors()) {

            log.info("errors={}", bindingResult);
            Member member = memberService.getMember(principal);
            model.addAttribute("member", member.getName());
            return "board/boardForm";
        }

        Board board = boardService.createBoard(boardFormDto, principal.getName());

        if(!CollectionUtils.isEmpty(boardFileList)) {

            boardFormDto.setBoard(board);
            boardFileService.storeFiles(boardFileList, board);
        }

        return "redirect:/board/detail/"+ board.getBoardId();
    }

    @GetMapping("/detail/{id}")
    public String detailBoard(@PathVariable("id") long id, Model model, Principal principal){

        BoardResponseDto findBoardDetail = boardService.getBoardDetail(id);

        model.addAttribute("member", memberService.getMember(principal));
        model.addAttribute("board", findBoardDetail);
        model.addAttribute("boardFileList", findBoardDetail.getBoardFileDtoList());

        return "board/boardDetail";
    }

    @GetMapping("/update/{id}")
    public String updateBoard(@PathVariable("id") Long id, Model model){


        BoardResponseDto findBoardDetail = boardService.getBoardDetail(id);

        model.addAttribute("board", findBoardDetail);
        model.addAttribute("boardFileList", findBoardDetail.getBoardFileDtoList());

        return "board/boardUpdate";
    }

    @ResponseBody
    @PostMapping("/update/{id}")
    public void updateBoard(@PathVariable("id") Long id, @ModelAttribute("board") BoardUpdateDto boardUpdateDto,
                              List<MultipartFile> boardFileList) throws IOException {

        Board board = boardService.updateBoard(boardUpdateDto, id);
        boardFileService.updateBoardFile(board, boardFileList, boardUpdateDto.getFileIdList());
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public void deleteBoard(@PathVariable("id") Long id){

        boardService.deleteBoard(id);
    }

    @ResponseBody
    @GetMapping("/file/download/{id}")
    public ResponseEntity<String> fileDownload(@PathVariable("id") String id) throws MalformedURLException {

        BoardFileDownloadDto downloadFileInfo = boardFileService.downloadBoardFile(id);


        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, downloadFileInfo.getContentDisposition())
                .body(downloadFileInfo.getResource());
    }
}

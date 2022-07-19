package com.shop.handler;

import com.shop.global.error.ErrorResponse;
import com.shop.global.error.exception.BoardNotFoundException;
import com.shop.global.error.exception.ErrorCode;
import com.shop.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.nio.file.FileSystemNotFoundException;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    private final BoardService boardService;

    /**
     *  javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
     *  HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
     *  주로 @RequestBody, @RequestPart 어노테이션에서 발생
     */

    @ExceptionHandler(FileSystemNotFoundException.class)
    protected ResponseEntity<?> handleFileSystemNotFoundException(FileSystemNotFoundException e) {

        log.error("handleFileSystemNotFoundException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.FILE_NOT_FOUND);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BoardNotFoundException.class)
    protected ModelAndView handleBoardNotFoundException(BoardNotFoundException e){

        log.error("handleBoardNotFoundException", e);

        ModelAndView mav = new ModelAndView();

        mav.setViewName("/board/boardlist");
        mav.addObject("boardList", boardService.getBoardList());
        mav.addObject("error", e.getMessage());

        return mav;
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {

        log.error("handleEntityNotFoundException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

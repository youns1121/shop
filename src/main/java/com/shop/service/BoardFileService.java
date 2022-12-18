package com.shop.service;

import com.shop.domain.Board;
import com.shop.domain.BoardFile;
import com.shop.dto.BoardFileDownloadDto;
import com.shop.dto.BoardFileDto;
import com.shop.repository.BoardFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystemNotFoundException;
import java.util.*;

@RequiredArgsConstructor
@Service
public class BoardFileService {

    private final BoardFileRepository boardFileRepository;

    @Value("D:/shop/board/")
    private String uploadPath;

    @Transactional
    public void updateBoardFile(Board board, List<MultipartFile> boardFileList, List<Long> fileIdList) throws IOException {

        List<BoardFile> findBoardFileList = getBoardFileList(board);

        notExistFileDelete(fileIdList, findBoardFileList);

        if(! CollectionUtils.isEmpty(boardFileList)) {
            storeFiles(boardFileList, board);
        }
    }

    private void notExistFileDelete(List<Long> fileIdList, List<BoardFile> findBoardFileList) {

        for (BoardFile boardFile : findBoardFileList) {
            if (!fileIdList.contains(boardFile.getBoardFileId())) {
                boardFileRepository.delete(boardFile);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<BoardFile> getBoardFileList(Board board){

        return boardFileRepository.findByBoard(board);
    }

    public String getFullPath(String fileName) {

        return uploadPath + fileName;
    }

    @Transactional
    public void storeFiles(List<MultipartFile> multipartFiles, Board board) throws IOException {

        List<BoardFileDto> boardFileList = new ArrayList<>();

            for (MultipartFile multipartFile : multipartFiles) {
                if(!multipartFile.isEmpty()) {
                    boardFileList.add(storeFile(multipartFile, board));
                }
            }

        boardFileRepository.saveAll(getBoardFiles(boardFileList));
    }

    private List<BoardFile> getBoardFiles(List<BoardFileDto> boardFileList) {

        List<BoardFile> boardFileResult = new ArrayList<>();

        for(BoardFileDto boardFileDto : boardFileList){

             boardFileResult.add(BoardFile.from(boardFileDto));
        }
        return boardFileResult;
    }

     
    public BoardFileDto storeFile(MultipartFile multipartFile, Board board) throws IOException {

        String originalFilename = multipartFile.getOriginalFilename();
        String extensionName = extractExt(originalFilename);
        String storeFileName = UUID.randomUUID().toString();

        multipartFile.transferTo(new File(getFullPath(storeFileName +"."+  extensionName)));

        return BoardFileDto.builder()
                .boardFileName(originalFilename)
                .boardFileStoreName(storeFileName)
                .boardFileExtension(extensionName)
                .boardFileSize(multipartFile.getSize())
                .boardFilePath(uploadPath)
                .board(board)
                .build();
    }

     
    public String extractExt(String originalFileName) {

        return originalFileName.substring(originalFileName.lastIndexOf(".") + 1)
                .toLowerCase(Locale.ROOT);
    }

    public BoardFileDownloadDto downloadBoardFile(String fileId) throws MalformedURLException {

        BoardFile boardFile = boardFileRepository.findById(Long.valueOf(fileId))
                .orElseThrow(() -> new FileSystemNotFoundException("존재하지 않는 파일입니다."));

        String storeFileName = boardFile.getBoardFileStoreName() + "." + boardFile.getBoardFileExtension();
        String uploadFileName = boardFile.getBoardFileName();

        UrlResource resource = new UrlResource("file:" + getFullPath(storeFileName));
        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        return BoardFileDownloadDto.builder()
                .resource(String.valueOf(resource))
                .contentDisposition(contentDisposition)
                .build();
    }
    
}

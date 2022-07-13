package com.shop.service;

import com.shop.domain.BoardFile;
import com.shop.dto.BoardFileDownloadDto;
import com.shop.dto.BoardFileDto;
import com.shop.dto.form.BoardFormDto;
import com.shop.repository.BoardFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RequiredArgsConstructor
@Service
public class BoardFileService {
    
    private final BoardFileRepository boardFileRepository;

    @Value("C:/Users/daema/ysmin/")
    private String uploadPath;

     
    public String getFullPath(String fileName) {

        return uploadPath + fileName;
    }


    @Transactional
    public void storeFiles(List<MultipartFile> multipartFiles, BoardFormDto boardFormDto) throws IOException {

        List<BoardFileDto> boardFileList = new ArrayList<>();

            for (MultipartFile multipartFile : multipartFiles) {
                if(!multipartFile.isEmpty()) {
                    boardFileList.add(storeFile(multipartFile, boardFormDto));
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

     
    public BoardFileDto storeFile(MultipartFile multipartFile, BoardFormDto boardFormDto) throws IOException {

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
                .board(boardFormDto.getBoard())
                .build();
    }


    public List<BoardFileDto> getBoardFileList(int id) {

        return Collections.emptyList();
    }

     
//    public BoardFileDto getBoardFile(int id) {
//
//        return boardFileMapper.selectBoardFile(id);
//    }

     
    public String extractExt(String originalFileName) {

        return originalFileName.substring(originalFileName.lastIndexOf(".") + 1)
                .toLowerCase(Locale.ROOT);
    }

    @Transactional
//    public void deleteFile(int id) {
//        BoardFileVo boardFileVo = boardFileMapper.selectBoardFile(id);
//        File file = new File(uploadPath + boardFileVo.getStoreFileName() );
//        if(file.exists()){
//            file.delete();
//        }
//        boardFileRepository.delete(id);
//
//    }

     
    public BoardFileDownloadDto downloadBoardFile(String fileId) throws MalformedURLException {
        BoardFile boardFile = boardFileRepository.findById(Long.valueOf(fileId))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 파일입니다."));

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

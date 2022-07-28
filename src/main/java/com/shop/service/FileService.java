package com.shop.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@Log
public class FileService {

    public String getFileFullPath(String fileName, String uploadPath){
        return uploadPath + fileName;
    }

    public String uploadFile(String uploadPath, String originalFileName, byte[] fileDate) throws IOException {

        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = UUID.randomUUID() + extension; //UUID로 받은 값과 원래 파일의 이름의 확장자를 조합해서 저장될 파일 이름을 만듬
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl); // FileOutputStream는 바이트 단위의 출력을 내보내는 클래스, 생성자로 파일이 저장될 위치와 파일의 이름을 넘겨 파일에 쓸 파일 출력 스트림을 만듬
        fos.write(fileDate);
        fos.close();

        return savedFileName;
    }

    public void deleteFile(String filePath) throws Exception{

        File deleteFile = new File(filePath); //파일이 저장된 경로를 이용파여 파일 객체를 생성

        if(deleteFile.exists()){ //해당 파일이 존재하면 파일을 삭제
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        }else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}

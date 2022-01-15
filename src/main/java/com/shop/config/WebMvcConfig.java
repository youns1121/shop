package com.shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${custom.path.uploadPath=file:///D:/shop/}") /*application.yml에 설정한 "uploadPath" 프로퍼티 값을 읽어옴*/
    String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**") /*웹 브라우저에 입력하는 url /images로 시작하는 경우 uploadPath에 설정한 폴더를 기준으로 파일을 읽어오도록 설정합니다*/
                .addResourceLocations(uploadPath); /*로컬에 저장된 파일을 읽어올 root 경로를 설정합니다*/
    }
}

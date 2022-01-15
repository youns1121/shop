package com.shop.controller;


import com.shop.dto.form.MemberFormDto;
import com.shop.domain.Member;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@SpringBootTest
@RequiredArgsConstructor
@AutoConfigureMockMvc
@Transactional
class MemberControllerTest {

    @Autowired
    private  MemberService memberService;

    @Autowired
    private MockMvc mockMvc; // 가짜 객체, 웹브라우저에서 요청을 하는 것처럼 테스트 가능

    @Autowired
    private  PasswordEncoder passwordEncoder;

    public Member createMember(String email, String password){ //로그인 전 회원을 등록하는 메서드
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail(email);
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword(password);
        Member member = Member.createMember(memberFormDto, passwordEncoder);
        return memberService.saveMember(member);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginSuccessTest() throws Exception{
        String email = "test@email.com";
        String password = "1234";
        this.createMember(email, password);
        mockMvc.perform(formLogin().userParameter("email")
                .loginProcessingUrl("/members/login") // 회원 가입 메소드를 실행 후 가입된 회원 정보로 로그인이 되는지 테스트
                .user(email)
                .password(password))
                .andExpect(SecurityMockMvcResultMatchers.authenticated()); //로그인이 성공하여 인증되었다면 테스트 통과
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    public void loginFailTest() throws Exception {
        String email = "test@email.com";
        String password = "1234";
        this.createMember(email, password);
        mockMvc.perform(formLogin().userParameter("email")
                .loginProcessingUrl("/members/login")
                .user(email)
                .password("12345"))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
        /*회원 가입은 정상적 진행, 회원가입 시 입력한 비밀번호가 아닌
        다른 비밀번호를 로그인을 시도하여 인증되지 ㅇ낳은 결과 값이 출력 되어 테스트 통과*/
    }




}
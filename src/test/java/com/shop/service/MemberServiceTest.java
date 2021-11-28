package com.shop.service;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember(){

        MemberFormDto memberFormDto = new MemberFormDto();

        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("테스트");
        memberFormDto.setAddress("부천시 여월동");
        memberFormDto.setPassword("1234");

        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void saveMemberTest(){
        Member member = createMember();
        Member saveMember = memberService.saveMember(member);

        //실제 저장된 데이터를 비교 : 첫 번째 파라미터는 기대값, 두 번째는 실제로 저장된 값
        assertEquals(member.getEmail(), saveMember.getEmail()); // 이메일 비교
        assertEquals(member.getName(), saveMember.getName());
        assertEquals(member.getAddress(), saveMember.getAddress());
        assertEquals(member.getPassword(), saveMember.getPassword());
        assertEquals(member.getMemberRole(), saveMember.getMemberRole());

    }

    @Test
    @DisplayName("증복 회원 가입 테스트")
    public void saveDuplicateMemberTest(){

        Member member1 = createMember();
        Member member2 = createMember();

        memberService.saveMember(member1);

        //첫 번째 발생할 예외 타입
        Throwable e = assertThrows(IllegalStateException.class, () -> {
            memberService.saveMember(member2);});

        //첫 번째 : 예상, 두 번째 : 실제 결과
        assertEquals("이미 가입된 회원입니다.", e.getMessage());

    }


}
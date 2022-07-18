package com.shop.service;

import com.shop.domain.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    /*UserDetailsService : DB에서 회원 정보를 가져오는 역할을 담당*/


    private final MemberRepository memberRepository;

    public Member saveMember(Member member){ /*회원 생성*/
        validateDuplicateMember(member);

        return memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public Member getMember(Principal principal) {
        return memberRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("로그인을 해주세요"));
    }

    @Transactional(readOnly = true)
     public void validateDuplicateMember(Member member){ /*중복 회원 검사*/

        memberRepository.findByEmail(member.getEmail())
                .orElseThrow(() -> new IllegalStateException("이미 가입된 회원입니다."));
    }

    @Override
    /*회원정보를 조회하여 사용자의 정보와 권한을 갖는 UserDetails 인터페이스를 반환*/
    /*메소드를 오버라이딩, 로그인할 유저의 email을 파라미터로 전달받음*/
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { /*메소드를 오버라이딩, */
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        return User.builder() /*UserDetail을 구현하고 있는 User 객체를 반환*/
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getMemberRole().toString())
                .build();
    }
}

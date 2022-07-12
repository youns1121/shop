package com.shop.config;

import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MemberService memberService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/members/login") //로그인 페이지 URL 설정
                .defaultSuccessUrl("/") //로그인 성공시 이동할 URL 설정
                .usernameParameter("email") // 로그인 시 사용할 파라미터 이름으로 email을 지정
                .failureUrl("/members/login/error") //로그인 실패 시 이동할 URL 설정
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) //로그아웃 URL을 설정
                .logoutSuccessUrl("/") // 로그아웃 성공 시 이동할 URL을 설정

        ;

        http.authorizeRequests() //시큐리티 처리에 HttpSevletRequest를 이용
                //perminAll()을 통해 모든 사용자가 인증(로그인)없이 해당 경로에 접근할 수 있도록 설정함, 메인 페이지, 회원관련 URL,
                // 상품 상세페이지, 상품 이미지를 불러오는 경로에 해당
                .mvcMatchers("/", "/members/**", "/item/**", "images/**").permitAll()
                .mvcMatchers("/admin/**").hasRole("ADMIN") //admin으로 시작하는 경로는 해당 계정이 ADMIN 일경우에만 접근 가능
                .anyRequest().authenticated() // 나머지 경로들은 모두 인증을 요구하도록 설정

        ;

        http.exceptionHandling()
                .authenticationEntryPoint(
                        (new CustomAuthenticationEntryPoint())) //인증되지 않은 사용자가 리소스에 접근하였을 때 수행되는 핸들러를 등록
        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception{
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**"); // static 디렉터리의 하위 파일은 인증을 무시
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        /**
         * 인증은 AuthenticationManagerBuilder가 AuthenticationManager를 생성
         * userDetailService를 구현하고 있는 객체로 memberService를 지정, 비밀번호를 암호화를 위해 passwordEncoder를 지정
         */

        auth.userDetailsService(memberService)
                .passwordEncoder(passwordEncoder());
    }


}

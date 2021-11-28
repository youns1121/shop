package com.shop.entity;

import com.shop.dto.MemberFormDto;
import com.shop.enums.MemberRole;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;


@Getter
@Setter
@Table(name="member")
@Entity
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="member_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "address", nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;



    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){

        Member member = new Member();

        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());

        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setMemberRole(MemberRole.USER);

        return member;
    }

//    @Builder
//    public void createMember(MemberDto memberDto, PasswordEncoder passwordEncoder){
//        this.name= memberDto.getName();
//        this.email= memberDto.getEmail();
//        this.email = memberDto.getEmail();
//        this.address = memberDto.getAddress();
//        this.password = passwordEncoder.encode(memberDto.getPassword());
//        this.memberRole = MemberRole.USER;
//    }

}

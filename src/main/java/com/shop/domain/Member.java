package com.shop.domain;


import com.shop.domain.comm.BaseEntity;
import com.shop.dto.form.MemberFormDto;
import com.shop.enums.MemberRole;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Getter
@Setter
@Table(name="member")
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="member_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name="email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "address")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role")
    private MemberRole memberRole;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){

        String password = passwordEncoder.encode(memberFormDto.getPassword());

        return Member.builder()
                .name(memberFormDto.getName())
                .email(memberFormDto.getEmail())
                .address(memberFormDto.getAddress())
                .password(memberFormDto.getPassword())
                .memberRole(MemberRole.ADMIN)
                .password(password)
                .build();
    }


}

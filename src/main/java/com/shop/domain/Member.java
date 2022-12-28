package com.shop.domain;


import com.shop.domain.comm.BaseEntity;
import com.shop.domain.embedded.Address;
import com.shop.domain.embedded.PhoneNumber;
import com.shop.dto.form.MemberFormDto;
import com.shop.enums.MemberRole;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;


@NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
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

    @Column(name = "picture")
    private String picture;

    @Column(name = "address")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role")
    private MemberRole memberRole;

    @Embedded
    private Address addressAttr;

    @Embedded
    private PhoneNumber phoneNumber;

    @Builder
    public Member(Long id, String name, String email, String password, String picture, String address, MemberRole memberRole, Address addressAttr, PhoneNumber phoneNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.picture = picture;
        this.address = address;
        this.memberRole = memberRole;
        this.addressAttr = addressAttr;
        this.phoneNumber = phoneNumber;
    }

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){

        return Member.builder()
                .name(memberFormDto.getName())
                .email(memberFormDto.getEmail())
                .address(memberFormDto.getAddress())
                .password(memberFormDto.getPassword())
                .memberRole(MemberRole.ADMIN)
                .password(passwordEncoder.encode(memberFormDto.getPassword()))
                .phoneNumber(PhoneNumber.create(memberFormDto.getPhoneNum1(), memberFormDto.getPhoneNum2(), memberFormDto.getPhoneNum3()))
                .picture(memberFormDto.getPicture())
                .build();
    }

    public Member update(String name, String picture) {
        this.name = name;
        this.picture = picture;

        return this;
    }

    public Member updateMember(MemberFormDto memberFormDto) {

        return Member.builder()
                .name(memberFormDto.getName())
                .email(memberFormDto.getEmail())
                .address(memberFormDto.getAddress())
                .password(memberFormDto.getPassword())
                .memberRole(MemberRole.ADMIN)
                .phoneNumber(PhoneNumber.create(memberFormDto.getPhoneNum1(), memberFormDto.getPhoneNum2(), memberFormDto.getPhoneNum3()))
                .picture(memberFormDto.getPicture())
                .build();
    }

    public String getRoleKey(){
        return this.memberRole.name();
    }
}

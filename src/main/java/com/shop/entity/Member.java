package com.shop.entity;

import com.shop.enums.MemberRole;

import javax.management.relation.Role;
import javax.persistence.*;

@Table(name="member")
@Entity
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="member_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;


}

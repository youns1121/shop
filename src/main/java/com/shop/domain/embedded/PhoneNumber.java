package com.shop.domain.embedded;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@NoArgsConstructor
@Getter
@Embeddable
public class PhoneNumber {

    @Column(name = "phone_num")
    private String phoneNum;

    @Column(name = "phone_num1")
    private String phoneNum1;

    @Column(name = "phone_num2")
    private String phoneNum2;

    @Column(name = "phone_num3")
    private String phoneNum3;

    @Builder
    public PhoneNumber(String phoneNum1, String phoneNum2, String phoneNum3) {

        if(StringUtils.hasText(phoneNum1) && StringUtils.hasText(phoneNum2) && StringUtils.hasText(phoneNum3)){
            this.phoneNum = phoneNum1.concat(phoneNum2).concat(phoneNum3);
        }

        this.phoneNum1 = phoneNum1;
        this.phoneNum2 = phoneNum2;
        this.phoneNum3 = phoneNum3;
    }

    public static PhoneNumber create(String phoneNum1, String phoneNum2, String phoneNum3){
        return new PhoneNumber(phoneNum1, phoneNum2, phoneNum3);
    }
}

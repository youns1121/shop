package com.shop.domain.embedded;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@NoArgsConstructor
@Embeddable
public class Address {

    private String zipCode;

    private String addressMain;

    private String addressDetail;

    @Builder
    public Address(String zipCode, String addressMain, String addressDetail) {
        this.zipCode = zipCode;
        this.addressMain = addressMain;
        this.addressDetail = addressDetail;
    }
}

package com.hello.entity;

import javax.persistence.Embeddable;

@Embeddable // 임베디드 값 타입을 정의하는 곳에 표시
public class Address {

    private String city;
    private String street;
    private String zipcode;

    // 임베디드 타입 사용 시 기본 생성자 필수
    public Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getZipcode() {
        return zipcode;
    }
}



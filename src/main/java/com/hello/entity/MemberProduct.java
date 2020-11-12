package com.hello.entity;

import javax.persistence.*;

// 다대다 연관관계 매핑 시 연결 테이블을 엔티티로 생성
@Entity
public class MemberProduct {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;
}

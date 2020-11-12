package com.hello.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Locker {

    @Id @GeneratedValue
    private Long id;

    private String name;

    // 일대일 양방향 매핑 시 : 다대일 양방향처럼 연관관계의 주인을 명시한다
    @OneToOne(mappedBy = "locker")
    private Member member;
}

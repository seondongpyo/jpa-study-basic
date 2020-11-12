package com.hello.entity;

import javax.persistence.*;

@Entity
public class Locker {

    @Id @GeneratedValue
    private Long id;

    private String name;

    // 외래 키가 대상 테이블에 존재하는 일대일 양방향 연관관계 매핑의 경우
    //  - 외래 키를 Locker에서 관리하므로 연관관계의 주인은 Locker가 된다
    @OneToOne
    @JoinColumn(name = "MEMBER_ID")
//    @OneToOne(mappedBy = "locker") // 일대일 양방향 매핑 시 : 다대일 양방향처럼 연관관계의 주인을 명시한다
    private Member member;
}

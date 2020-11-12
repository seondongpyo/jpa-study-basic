package com.hello.entity;

import javax.persistence.*;

@Entity
@Table(name = "MEMBER_TEST")
public class Member {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    /*
        일대일 연관관계 매핑 정리

        1) 주 테이블(많이 조회하는)에 외래 키가 있는 경우
            - 주 객체가 대상 객체의 참조를 가지는 것처럼, 주 테이블에 외래 키를 두고 대상 테이블을 찾음
            - 객체 지향 개발자들이 선호
            - JPA 매핑 관리
            - 장점 : 주 테이블만 조회해도 대상 테이블에 데이터가 있는지 확인 가능
            - 단점 : 값이 없으면 외래 키에 null 허용

        2) 대상 테이블에 외래 키가 있는 경우
            - 전통적인 데이터베이스 개발자들이 선호
            - 일대일 단방향 매핑을 JPA에서 지원하지 않음 (양방향은 지원)
            - 장점 : 주 테이블과 대상 테이블을 일대일에서 일대다로 변경할 때 테이블 구조를 유지할 수 있음
            - 단점 : 프록시 기능의 한계로 인해 지연 로딩으로 설정해도 항상 즉시 로딩됨
     */

    // 외래 키가 대상 테이블에 있는 일대일 양방향 연관관계 매핑의 경우
    //  - 외래 키가 Locker에 있으므로, mappedBy 적용
    @OneToOne(mappedBy = "member")
//    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID", insertable = false, updatable = false)
    private Team team;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

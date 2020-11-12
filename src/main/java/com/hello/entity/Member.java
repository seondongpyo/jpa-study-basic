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
        일대일 연관관계 매핑
            - 다대일(@ManyToOne) 단방향 연관관계 매핑과 유사하다
            - 외래 키를 관리할 테이블로 주 테이블 또는 대상 테이블 중 하나를 선택할 수 있다
            - 외래 키가 있는 곳이 연관관계의 주인이다 (반대편은 mappedBy 적용)
            - 외래 키에 데이터베이스 유니크 제약조건을 추가한다
     */
    @OneToOne
    @JoinColumn(name = "LOCKER_ID")
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

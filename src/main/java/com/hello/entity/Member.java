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

    // 일대다 양방향 연관관계 매핑
    //  - 이런 매핑은 공식적으로 존재하지 않는다
    //  - 읽기 전용 필드를 사용해서 양방향처럼 사용하는 방법이다
    //  => 다대일 양방향을 사용하자
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

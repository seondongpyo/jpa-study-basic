package com.hello.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {

    @Id @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;

    private String name;

    // 일대다 단방향 연관관계 매핑 : 일(1)이 연관관계의 주인
    /*
        << 단점 >>
            - 엔티티가 관리하는 외래 키가 다른 테이블(Member)에 있음
            - 연관관계 관리를 위해 추가로 UPDATE 쿼리가 실행됨
            => 일대다 단방향보다는 다대일 양방향 매핑을 사용하자
     */
    @OneToMany
    @JoinColumn(name = "TEAM_ID") // @JoinColumn이 없으면 중간에 테이블을 하나 추가하는 조인 테이블 방식을 사용하게 됨
    private List<Member> members = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}

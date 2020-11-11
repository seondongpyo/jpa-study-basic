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

    /*
        << 양방향 연관관계 매핑 >>
            - 객체의 두 관계 중 하나를 연관관계의 주인으로 지정해야 한다.
            - 연관관계의 주인만이 외래 키를 관리(등록, 수정)한다.
            - 연관관계의 주인이 아닌 쪽은 읽기만 가능하다.
            - 연관관계의 주인은 mappedBy 속성을 사용하지 않는다.
            - 연관관계의 주인이 아니라면 mappedBy 속성으로 연관관계의 주인을 명시해야 한다.

        Q. 그럼 누구를 연관관계의 주인으로 지정해야 하는가?
            - 외래 키가 있는 곳을 연관관계의 주인으로 지정하라.
                : 실제 데이터베이스 상에는 Member 테이블에 Team 테이블의 외래 키인 'TEAM_ID' 컬럼이 있다
            - 여기서는 Member의 Team이 연관관계의 주인이다 (@JoinColumn(name = "TEAM_ID"))
     */
    @OneToMany(mappedBy = "team")   // "team" : 연관관계의 주인의 필드명
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

    // 연관관계 편의 메서드
    public void addMember(Member member) {
        member.setTeam(this);
        members.add(member);
    }
}

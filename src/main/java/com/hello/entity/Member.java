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

//    @Column(name = "TEAM_ID")
//    private Long teamId;

    /*
        << 양방향 연관관계 매핑 >>
            - 외래 키(TEAM_ID)가 Member에 있으므로 연관관계의 주인으로 지정한다
            - 연관관계의 주인이므로 mappedBy 속성을 사용하지 않는다
     */
    @ManyToOne  // Member 입장에서는 여러 Member가 하나의 Team에 속하므로 N : 1
    @JoinColumn(name = "TEAM_ID")   // Team 엔티티와 join할 컬럼명 지정
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

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}

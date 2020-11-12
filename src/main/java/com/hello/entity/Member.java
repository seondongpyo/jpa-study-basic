package com.hello.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MEMBER_TEST")
public class Member {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    /*
        다대다 연관관계 매핑
            - 관계형 데이터베이스는 정규화된 테이블 2개로 다대다 관계를 표현할 수 없음
                => 연결 테이블을 추가하여 다대다 관계를 일대다, 다대일로 풀어서 처리
            - 하지만 객체는 컬렉션을 사용하여 객체 2개로 다대다 관계를 처리할 수 있음
                => 그러나 편리해 보이지만, 실무에서 사용 X
            - 연결 테이블이 단순히 연결만 하고 끝나지 않음
            - 주문 시간, 수량 같은 데이터가 들어올 수 있음 (매핑 정보 이외의 추가 정보를 넣는 게 불가능)
     */
    @ManyToMany
    @JoinTable(name = "MEMBER_PRODUCT") // 연결 테이블 생성
    private List<Product> products = new ArrayList<>();

    @OneToOne(mappedBy = "member")
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

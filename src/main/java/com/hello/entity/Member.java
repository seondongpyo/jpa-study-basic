package com.hello.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Member extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    // 값 타입 컬렉션 시작 =====================================
    @ElementCollection
    @CollectionTable(
        name = "FAVORITE_FOOD", // 테이블명을 지정
        joinColumns = @JoinColumn(name = "MEMBER_ID")
    )
    @Column(name = "FOOD_NAME")
    private Set<String> favoriteFoods = new HashSet<>();

//    @ElementCollection
//    @CollectionTable(
//        name = "ADDRESS",
//        joinColumns = @JoinColumn(name = "MEMBER_ID")
//    )
//    private List<Address> addressHistory = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "MEMBER_ID")
    private List<AddressEntity> addressHistory = new ArrayList<>();
    // 값 타입 컬렉션 끝 ========================================

    // Period 임베디드 값 타입
    @Embedded // 값 타입을 사용하는 곳에 표시
    private Period workPeriod;

    // Address 임베디드 값 타입
    @Embedded // 값 타입을 사용하는 곳에 표시
    private Address homeAddress;

    // Q. 한 엔티티에서 같은 값 타입을 사용하려면? -> 컬럼 명이 중복된다.
    // A. @AttributeOverrides, @AttributeOverride를 사용해서 컬럼 이름을 재정의 할 수 있다
    @AttributeOverrides({
        @AttributeOverride(name = "city", column = @Column(name = "WORK_CITY")),
        @AttributeOverride(name = "street", column = @Column(name = "WORK_STREET")),
        @AttributeOverride(name = "zipcode", column = @Column(name = "WORK_ZIPCODE"))
    })
    @Embedded
    private Address workAddress;

    // 연결 테이블과의 관계를 일대다, 다대일로 풀어낸다
    @OneToMany(mappedBy = "member")
    private List<MemberProduct> memberProducts = new ArrayList<>();

    @OneToOne(mappedBy = "member")
    private Locker locker;

    @ManyToOne(
//        fetch = FetchType.LAZY  // 지연 로딩 설정 (Team을 우선 프록시로 조회한다)
        fetch = FetchType.EAGER  // 즉시 로딩 설정 (Member와 Team을 같이 조회한다)
    )
    @JoinColumn(name = "TEAM_ID")
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

    public List<MemberProduct> getMemberProducts() {
        return memberProducts;
    }

    public void setMemberProducts(List<MemberProduct> memberProducts) {
        this.memberProducts = memberProducts;
    }

    public Locker getLocker() {
        return locker;
    }

    public void setLocker(Locker locker) {
        this.locker = locker;
    }

    public Period getWorkPeriod() {
        return workPeriod;
    }

    public void setWorkPeriod(Period workPeriod) {
        this.workPeriod = workPeriod;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Set<String> getFavoriteFoods() {
        return favoriteFoods;
    }

    public void setFavoriteFoods(Set<String> favoriteFoods) {
        this.favoriteFoods = favoriteFoods;
    }

    public List<AddressEntity> getAddressHistory() {
        return addressHistory;
    }

    public void setAddressHistory(List<AddressEntity> addressHistory) {
        this.addressHistory = addressHistory;
    }
}

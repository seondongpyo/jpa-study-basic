package com.hello.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Parent {

    @Id @GeneratedValue
    private Long id;

    private String name;

    /*
        << 영속성 전이 설정 시 CASCADE의 종류 >>
        1. ALL : 모두 적용
        2. PERSIST : 영속
        3. REMOVE : 삭제
        4. MERGE : 병합
        5. REFRESH
        6. DETACH
     */
    @OneToMany(
        mappedBy = "parent",
        cascade = CascadeType.ALL // 영속성 전이 설정
//        orphanRemoval = true // 고아 객체 설정
    )
    private List<Child> childList = new ArrayList<>();

    public void addChild(Child child) {
        childList.add(child);
        child.setParent(this);
    }

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

    public List<Child> getChildList() {
        return childList;
    }

    public void setChildList(List<Child> childList) {
        this.childList = childList;
    }
}

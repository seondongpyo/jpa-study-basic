package com.hello.entity.item;

import javax.persistence.*;

/*
    << 상속관계 매핑 전략 정리 >>
    - 관계형 데이터베이스에는 상속 관계가 없지만, 슈퍼 타입과 서브 타입이라는 모델링 기법이 객체의 상속과 유사하다.
        => 상속관계 매핑이란 객체의 상속 및 구조와, DB의 슈퍼 타입 및 서브 타입 관계를 매핑하는 것이다.

    1. 조인 전략 (추천 및 지향)
        1) 장점
            - 데이터베이스 정규화가 잘 되어있다.
            - 외래 키 잠조 시 무결성 제약조건 활용이 가능하다.
            - 저장 공간을 효율적으로 사용할 수 있다.
        2) 단점
            - 조회 시 조인을 많이 사용하기 때문에 성능이 (약간) 저하될 수 있다.
            - 조회하는 쿼리가 복잡하다.
            - 데이터를 저장할 때 INSERT 쿼리가 2번 호출된다.

    2. 단일 테이블 전략
        1) 장점
            - 조인이 필요없으므로 일반적으로 조회 성능이 빠르다.
            - 조회 쿼리가 단순하다.
        2) 단점
            - 공통 컬럼을 제외한, 자식 엔티티가 매핑한 컬럼은 모두 NULL을 허용해야 한다.
            - 하나의 테이블에 모든 컬럼을 저장하므로 테이블이 커질 수 있으며, 상황에 따라서 조회 성능이 오히려 느려질 수 있다.

    3. 구현 클래스마다 테이블 생성 전략 (지양, 비추천)
        1) 장점
            - 서브 타입을 명확하게 구분해서 처리할 때 효과적이다.
            - NOT NULL 제약조건을 사용할 수 있다.
        2) 단점
            - 여러 자식 테이블을 함께 조회해야 할 때 성능이 느리다. (UNION 쿼리)
            - 자식 테이블을 통합해서 쿼리를 호출하기가 어렵다.
 */

@Entity
@Inheritance(
    strategy = InheritanceType.JOINED // 조인 전략 : 공통 컬럼들은 Item, 나머지 컬럼들은 각각의 테이블(ALBUM, MOVIE, BOOK)로 변환
//    strategy = InheritanceType.SINGLE_TABLE // 단일 테이블 전략 (기본값) : 한 테이블에 모든 컬럼을 몰아넣어서 생성
//    strategy = InheritanceType.TABLE_PER_CLASS // 구현 클래스들을 각각의 테이블로 생성
)
@DiscriminatorColumn // 하위 테이블을 구분하는 용도의 컬럼(기본값 : DTYPE)을 추가
public class Item {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private int price;

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}

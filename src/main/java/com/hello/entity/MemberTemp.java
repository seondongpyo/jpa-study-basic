package com.hello.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
//@SequenceGenerator( // SEQUENCE 전략
//    name = "MEMBER_SEQ_GENERATOR",
//    sequenceName = "MEMBER_SEQ",    // 매핑할 데이터베이스 시퀀스 이름
//    initialValue = 1,   // 시퀀스 생성 시 처음 시작하는 수
//    allocationSize = 1  // 시퀀스를 호출할 때마다 증가할 수 (기본값: 50, 성능 최적화에 사용됨)
//)
//@TableGenerator( // TABLE 전략
//    name = "MEMBER_SEQ_GENERATOR",
//    table = "MY_SEQUENCES", // 키 생성 테이블명
//    pkColumnValue = "MEMBER_SEQ",   // 키로 사용할 값의 이름
//    allocationSize = 1  // 시퀀스를 호출할 때마다 증가할 수 (기본값: 50, 성능 최적화에 사용됨)
//)
@Table(name = "MEMBER_TEMP")
public class MemberTemp {

    @Id // 기본 키를 직접 할당할 경우
    @GeneratedValue(    // @GeneratedValue : 기본 키를 자동 생성할 경우
        strategy = GenerationType.IDENTITY
//        strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR" // SEQUENCE
//        strategy = GenerationType.TABLE, generator = "MEMBER_SEQ_GENERATOR" // TABLE

        /*
            << 기본 키 생성 전략 >>
            1) IDENTITY
                - 기본 키 생성을 데이터베이스에 위임
                - 주로 MySQL, PostgreSQL, SQL Server, DB2에서 사용 (ex. MySQL의 AUTO_INCREMENT)

                << 참고 >>
                - JPA는 보통 트랜잭션 커밋 시점에 INSERT SQL이 실행되는데,
                  AUTO_INCREMENT는 데이터베이스에 INSERT SQL을 실행한 이후에 ID 값을 알 수 있음
                  영속성 컨텍스트에서 해당 엔티티를 관리하려면 ID 값이 필요하므로,
                  예외적으로 em.persist()를 호출 시 em.commit()을 호출하기 전에 먼저 INSERT SQL을 실행한 뒤,
                  엔티티 저장 시 생성된 ID 값을 통해 영속성 컨텍스트에서 관리한다
                  즉, 쓰기 지연 SQL 저장소 전략을 사용할 수 없다는 단점이 있다

            2) SEQUENCE
                - 유일한 값을 순서대로 생성하는 특별한 데이터베이스 오브젝트 (ex. Oracle의 시퀀스)
                - Oracle, PostgreSQL, DB2, H2 데이터베이스에서 사용
                - @SequenceGenerator를 통해 매핑 가능

                << 참고 >>
                - IDENTITY 전략과 마찬가지로, 영속성 컨텍스트에서 엔티티를 관리하려면 ID 값을 알아야 하는데,
                  시퀀스 값은 데이터베이스에서 알 수 있으므로 em.persist() 전에 먼저 시퀀스 값을 조회하여 가져온다.
                  단, INSERT SQL을 실행하는 건 아니므로 쓰기 지연 SQL 저장소 전략을 사용할 수 있다

                  Q. 그럼 매번 시퀀스를 조회할 때마다 데이터베이스 네트워크를 타는데 성능 이슈가 발생하지 않을까?
                  A. allocationSize (기본값 : 50) 옵션을 사용하면 됨
                     (미리 특정 사이즈만큼 시퀀스를 데이터베이스에서 늘려놨다가
                      해당 숫자만큼 도달하면 시퀀스를 다시 특정 사이즈만큼 늘려서 생성하는 방식)

            3) TABLE
                - 키 생성 전용 테이블을 하나 만들어서 데이터베이스 시퀀스를 흉내내는 전략
                - 모든 데이터베이스에 적용이 가능하다는 장점이 있으나, 성능이 떨어진다는 단점이 있음

            << 권장하는 식별자 전략 >>
                - 기본 키 제약 조건 : not null, 유일해야 하며 변하면 안 된다
                - 먼 미래까지 이 조건을 만족하는 자연 키는 찾기 어려우므로, 대체 키를 사용해야 한다
                - 권장 : Long 타입 + 대체 키 + 키 생성 전략 등을 조합하여 사용
         */
    )
    private Long id;

    @Column(
        name = "name"      // 필드와 매핑할 테이블의 컬럼 이름
//        insertable = false // 등록 가능 여부
//        updatable = false  // 수정 가능 여부
//        nullable = true    // null 값의 허용 여부, false 시 not null 제약 조건 부여
//        unique = true      // 간단한 제약 조건 부여 시 사용, 자주 사용되지 않음
//        columnDefinition = ""  // 데이터베이스 컬럼 정보를 직접 부여 가능 (ex. varchar(100) default 'empty')
//        length = 10        // 문자 길이 제약 조건, String 타입에만 사용 가능
//        precision = 0      // 소수점을 포함한 전체 자릿수, BigDecimal(또는 BigInteger) 타입에서 사용
//        scale = 0           // 소수의 자릿수, BigDecimal(또는 BigInteger) 타입에서 사용
    )
    private String username;

    private Integer age;

    @Enumerated( // @Enumerated : 열거(enum) 타입 사용, 단 EnumType 사용 시 String 사용 권장
//        EnumType.ORDINAL    // enum 순서를 데이터베이스에 저장(기본값), 사용 지양
                                // -> 새로운 enum 값이 추가될 경우 순서로 저장 시 기존 데이터와 꼬일 수 있다
        EnumType.STRING     // enum 이름을 데이터베이스에 저장
    )
    private RoleType roleType;

    @Temporal( // @Temporal : 날짜 타입 사용
//        TemporalType.DATE   // 날짜
//        TemporalType.TIME   // 시간
        TemporalType.TIMESTAMP // 날짜 + 시간
    )
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    // LocalDate 또는 LocalDateTime 사용 시에는 @Temporal 생략 가능
    private LocalDate testLocalDate;
    private LocalDateTime testLocalDateTime;

    @Lob    // @Lob : large object, 대용량 데이터 타입 (BLOB, CLOB) 사용
            // 필드 타입이 문자면 CLOB(String, char[], java.sql.CLOB),
            // 나머지는 BLOB(byte[], java.sql.BLOB)
    private String description;

    @Transient  // @Transient : 필드 매핑이 되지 않음, 주로 메모리에서만 임시용으로 사용할 경우
                // 실제 데이터베이스에 저장하지도, 조회되지도 않음
    private int temp;

    public MemberTemp() {
    }
}

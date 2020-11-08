package com.hello.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Member {

    @Id // Primary Key
    private Long id;

    @Column(
        name = "name",      // 필드와 매핑할 테이블의 컬럼 이름
        insertable = false, // 등록 가능 여부
        updatable = false,  // 수정 가능 여부
        nullable = true,    // null 값의 허용 여부, false 시 not null 제약 조건 부여
        unique = true,      // 간단한 제약 조건 부여 시 사용, 자주 사용되지 않음
        columnDefinition = "",  // 데이터베이스 컬럼 정보를 직접 부여 가능 (ex. varchar(100) default 'empty')
        length = 10,        // 문자 길이 제약 조건, String 타입에만 사용 가능
        precision = 0,      // 소수점을 포함한 전체 자릿수, BigDecimal(또는 BigInteger) 타입에서 사용
        scale = 0           // 소수의 자릿수, BigDecimal(또는 BigInteger) 타입에서 사용
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

    public Member() {
    }
}

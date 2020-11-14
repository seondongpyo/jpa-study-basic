package com.hello.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/*
    << @MappedSuperclass 정리 >>
    - <!주의!> 상속관계 매핑이 아니다.
    - 엔티티가 아니기 때문에, 테이블과 매핑되지 않는다. (BaseEntity 테이블이 생성되지 않는다)
    - 부모 클래스를 상속 받는 자식 클래스에 매핑 정보만 제공한다.
    - 엔티티가 아니므로 조회 및 검색이 불가능하다. (em.find(BaseEntity.class) 불가)
    - 직접 생성해서 사용할 일이 없으므로 추상 클래스로 사용하는 것을 권장한다.

    - 테이블과 관계가 없고, 단순히 엔티티가 공통으로 사용하는 매핑 정보를 모으는 역할을 한다.
        => 주로 등록일, 수정일, 등록자, 수정자 같은 전체 엔티티에서 공통으로 적용하는 정보를 모을 때 사용한다.
    - 참고 : @Entity 클래스는 엔티티나 @MappedSuperclass로 지정한 클래스만 상속이 가능하다.
 */

@MappedSuperclass // 공통 매핑 정보가 필요할 때 사용
public abstract class BaseEntity {

    @Column("CREATE_MEMBER")
    private String createdBy;

    @Column("UPDATE_MEMBER")
    private String lastModifiedBy;

    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}

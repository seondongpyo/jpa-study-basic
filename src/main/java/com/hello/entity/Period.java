package com.hello.entity;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable // 임베디드 값 타입을 정의하는 곳에 표시
public class Period {

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    // 임베디드 타입 사용 시 기본 생성자 필수
    public Period() {
    }

    public Period(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}

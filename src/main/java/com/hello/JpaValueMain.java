package com.hello;

public class JpaValueMain {

    /*
        << JPA의 데이터 타입 분류 >>
        1. 엔티티 타입
            - @Entity로 정의하는 객체
            - 데이터가 변해도 식별자로 지속해서 추적이 가능
                ex) 회원 엔티티의 키나 나이 값을 변경해도 식별자로 인식 가능

        2. 값 타입
            - int, Integer, String처럼 단순히 값으로 사용하는 자바 기본 타입이나 객체
            - 식별자가 없고 값만 있으므로 변경 시 추적이 불가능
                ex) 숫자 100을 200으로 변경하면 완전히 다른 값으로 대체됨

        << 값 타입의 분류 >>
        1. 기본값 타입
            - 자바 기본 타입(int, double)
            - 래퍼 클래스(Integer, Long)
            - String

        2. 임베디드 타입(embedded type, 복합 값 타입)
        3. 컬렉션 값 타입(collection value type)

        << 기본값 타입 >>
        1. 생명 주기를 엔티티에 의존한다 (ex. 회원 엔티티가 삭제되면 이름, 나이 필드도 함께 삭제된다)
        2. 값 타입은 공유하면 안 된다 (ex. 특정 회원의 이름을 변경 시, 다른 회원의 이름까지 변경되면 안 된다)
     */


    public static void main(String[] args) {

    }
}

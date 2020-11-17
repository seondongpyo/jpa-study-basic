package com.hello;

import com.hello.entity.Address;
import com.hello.entity.Member;
import com.hello.entity.Period;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

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
        1. 기본 값 타입
            - 자바 기본 타입(int, double)
            - 래퍼 클래스(Integer, Long)
            - String

            << 기본값 타입의 특징 >>
            - 생명 주기를 엔티티에 의존한다 (ex. 회원 엔티티가 삭제되면 이름, 나이 필드도 함께 삭제된다)
            - 값 타입은 공유하면 안 된다 (ex. 특정 회원의 이름을 변경 시, 다른 회원의 이름까지 변경되면 안 된다)

        2. 임베디드 타입(embedded type, 복합 값 타입)
            - 새로운 값 타입을 직접 정의할 수 있음 (JPA에서는 임베디드 타입이라고 한다)
            - 주로 기본 값 타입을 모아서 만들기 때문에 복합 값 타입이라고도 한다
            - 임베디드 타입은 int, String과 같은 값 타입이다

            << 임베디드 타입의 특징 >>
            - 재사용이 가능하다
            - 응집도가 높다
            - 해당 값 타입만 사용하는 의미 있는 메서드를 만들 수 있다
            - 임베디드 타입을 포함한 모든 값 타입은 해당 값 타입을 소유한 엔티티에 생명 주기를 의존한다

            << 임베디드 타입과 테이블 매핑 >>
            - 임베디드 타입은 엔티티의 값일 뿐이다
            - 임베디드 타입을 사용하기 전과 후에 매핑되는 테이블은 같다
            - 객체와 테이블을 아주 세밀하게(find-grained) 매핑하는 것이 가능하다
            - 잘 설계한 ORM 어플리케이션은 매핑한 테이블의 수보다 클래스의 수가 더 많다


        3. 컬렉션 값 타입(collection value type)
     */

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        try {
            Member member = new Member();
            member.setUsername("memberA");
            member.setHomeAddress(new Address("city", "street", "zipcode"));
            member.setWorkPeriod(new Period()); // 임베디드 타입의 값이 null이면 매핑한 컬럼의 값들도 모두 null
            em.persist(member);

            transaction.commit();

        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();

        } finally {
            em.close();
        }

        emf.close();
    }
}

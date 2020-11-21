package com.hello;

import com.hello.entity.Address;
import com.hello.entity.Member;
import com.hello.entity.Period;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class  JpaValueMain {

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
            - 값 타입을 하나 이상 저장할 때 사용
            - @ElementCollection, @CollectionTable 사용
            - 데이터베이스는 컬렉션을 같은 테이블에 저장할 수 없다
            - 컬렉션을 저장하기 위한 별도의 테이블이 필요하다
     */

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        try {
            Address address = new Address("city", "street", "zipcode");

            Member member1 = new Member();
            member1.setUsername("memberA");
            member1.setHomeAddress(address);
            member1.setWorkPeriod(new Period()); // 임베디드 타입의 값이 null이면 매핑한 컬럼의 값들도 모두 null
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("memberB");
            member2.setHomeAddress(address); // 임베디드 타입 같은 값 타입을 여러 엔티티에서 공유하면 위험하다!
            member2.setWorkPeriod(new Period());
            em.persist(member2);

            // 값 타입 공유에 의한 부작용(side effect) 발생
//            member1.getHomeAddress().setCity("newCity"); // member1과 member2 모두 update 쿼리가 호출된다

            // (당연한 말이지만) 영속성 컨텍스트를 비운 뒤, 하나의 멤버만 조회하여 수정하면 가능
//            em.flush();
//            em.clear();
//            Member findMember1 = em.find(Member.class, member1.getId());
//            findMember1.getHomeAddress().setCity("newCity"); // update 쿼리가 해당 엔티티에 대해서만 호출된다

            // !! 값 타입의 실제 인스턴스인 값을 공유하는 것은 위험하기 때문에, 값(인스턴스)을 복사해서 사용해야 한다 !!
//            Address newAddress = new Address(address.getCity(), address.getStreet(), address.getZipcode());
//            member2.setHomeAddress(newAddress);

            /*
                << 객체 타입의 한계 >>
                - 항상 값을 복사해서 사용하면 공유 참조로 인해 발생하는 부작용을 피할 수 있다.
                - 하지만 임베디드 타입처럼 사용자가 직접 정의한 값 타입은, 자바의 기본 타입이 아닌 객체 타입이다.
                - 자바의 기본 타입에 값을 대입하면 값을 복사하기 때문에 안전하지만, 객체는 참조 값을 대입하므로 위험하다.
                - 객체 타입은 참조 값을 직접 대입하는 것을 막을 방법이 없으므로 객체의 공유 참조는 피할 수 없다.
                    => 객체 타입을 수정할 수 없게, 즉 불변 객체로 만들면 된다!

                << 불변 객체 >>
                - 생성 시점 이후 절대 값을 변경할 수 없는 객체를 의미한다.
                - 값 타입은 불변 객체(immutable object)로 설계해야 한다.
                - 생성자로만 값을 설정하고, 수정자(setter)를 만들지 않거나 private하게 만들면 된다.
                   (참고) Integer, String은 자바가 제공하는 대표적인 불변 객체
                    => '불변'이라는 작은 제약을 통해 '부작용'이라는 큰 재앙을 막을 수 있다.
             */

            // Q. 그럼 수정자가 없는데, 값을 바꾸고 싶으면 어떻게 해야 하나?
            //  => 값을 완전히 새로 생성하여 넣어준다.
            Member member3 = new Member();
            member3.setHomeAddress(address);
            em.persist(member3);

            Address newAddress = new Address("newCity", address.getStreet(), address.getZipcode());
            member3.setHomeAddress(newAddress);

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

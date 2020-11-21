package com.hello;

import com.hello.entity.Address;
import com.hello.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Set;

public class JpaCollectionMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        try {
            // 값 타입 컬렉션 저장
            Member member = new Member();
            member.setUsername("member1");
            member.setHomeAddress(new Address("homeCity", "street", "zipcode"));

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("족발");
            member.getFavoriteFoods().add("피자");

            member.getAddressHistory().add(new Address("old1", "street", "zipcode"));
            member.getAddressHistory().add(new Address("old2", "street", "zipcode"));

            em.persist(member); // Member 엔티티만 저장했을 뿐인데, 값 타입 컬렉션도 같이 저장된다?
            // => 값 타입 컬렉션도 생명 주기가 Member에 의해서 관리되기 때문이다
            // < 참고 > 값 타입 컬렉션은 영속성 전이(cascade) + 고아 객체 제거 기능을 필수로 가진다고 볼 수 있다

            em.flush();
            em.clear();

            // 값 타입 컬렉션 조회
            System.out.println("========================================");
            Member findMember = em.find(Member.class, member.getId()); // Member 엔티티만 조회된다?
            // => 값 타입 컬렉션도 지연 로딩 전략을 기본적으로 사용한다

            List<Address> addressHistory = findMember.getAddressHistory();
            for (Address address : addressHistory) { // 실제 값에 접근할 때 select 쿼리 호출
                System.out.println("address = " + address.getCity());
            }

            Set<String> favoriteFoods = findMember.getFavoriteFoods();
            for (String favoriteFood : favoriteFoods) { // 실제 값에 접근할 때 select 쿼리 호출
                System.out.println("favoriteFood = " + favoriteFood);
            }

            // (복습) 값 타입을 수정하려면?
//            findMember.getHomeAddress().setCity("newCity") // 잘못된 수정 방법 (값 타입은 immutable해야 한다)
            Address oldAddress = findMember.getHomeAddress();
            findMember.setHomeAddress(new Address("newCity", oldAddress.getStreet(), oldAddress.getZipcode()));

            // 값 타입 컬렉션 수정
            // 1. 좋아하는 음식을 치킨 -> 한식으로 수정하려면?
            findMember.getFavoriteFoods().remove("치킨");
            findMember.getFavoriteFoods().add("한식");

            // 2. 주소 이력에서 기존 주소 이력을 수정하려면?
            // (추가적으로 컬렉션의 remove() 동작 방식에 대해서 좀 더 공부해보자)
            findMember.getAddressHistory().remove(new Address("old1", "street", "zipcode"));
            findMember.getAddressHistory().add(new Address("newCity1", "street", "zipcode"));
            // -> 쿼리가 삭제 1번, 저장 1번 호출될 것으로 예상했는데, 삭제 1번은 맞는데 저장이 2번 호출됐다?
            //  => 하단의 3번째 제약사항 참고

            /*
                << 값 타입 컬렉션의 제약사항 >>
                    - 값 타입은 엔티티와 다르게 식별자 개념이 없다.
                    - 따라서 값은 변경하면 추적이 어렵다.
                    - (주의) 값 타입 컬렉션에 변경 사항이 발생하면, 주인 엔티티와 연관된 모든 데이터를 삭제하고(!),
                      값 타입 컬렉션에 있는 현재 값을 모두 다시 저장한다.
                    - !! 값 타입 컬렉션을 매핑하는 테이블은 모든 컬럼을 묶어서 기본 키를 구성해야 한다. !!
                      (null 입력 X, 중복 저장 X)
             */

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

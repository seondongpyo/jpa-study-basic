package com.hello;

import com.hello.entity.Child;
import com.hello.entity.Parent;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaCascadeMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        try {
            Child child1 = new Child();
            Child child2 = new Child();

            Parent parent = new Parent();
            parent.addChild(child1);
            parent.addChild(child2);

            // 영속성 전이 설정 전
//            em.persist(parent);
//            em.persist(child1);
//            em.persist(child2);
            // => Q. parent가 저장될 때 child도 같이 저장되게 할 수는 없을까?

            // 영속성 전이 설정 후
            em.persist(parent); // parent가 저장될 때 child도 같이 저장된다!

            /*
                << 영속성 전이 사용 시 주의 >>
                1. 영속성 전이는 연관관계를 매핑하는 것과 아무 관련이 없다.
                2. 엔티티를 영속화할 때 연관된 엔티티도 함께 영속화하는 편리함을 제공할 뿐이다.
             */

            em.flush();
            em.clear();

            /*
                << 고아 객체 >>
                - 참조가 제거된 엔티티는 다른 곳에서 참조하지 않는 고아 객체로 인식하여 삭제하는 기능
                - < 주의 > 참조하는 곳이 하나인 경우에 사용해야 한다.
                - < 주의 > 특정 엔티티가 개인 소유할 때 사용한다.
                - @OneToOne, @OneToMany만 가능하다.
             */
            Parent findParent1 = em.find(Parent.class, parent.getId());
            findParent1.getChildList().remove(0); // 연관관계가 끊어진(컬렉션에서 제거된) 엔티티가 삭제된다

            em.flush();
            em.clear();

            // < 참고 >
            // 개념적으로 부모를 제거하면 자식은 고아가 된다. 따라서 고아 객체 제거 기능을 활성화하면
            // 부모를 제거할 때 자식도 함께 제거된다. 이것은 CascadeType.REMOVE처럼 동작한다.
            Parent findParent2 = em.find(Parent.class, parent.getId());
            em.remove(findParent2); // 부모가 제거되면 자식도 함께 제거된다

            /*
                << 영속성 전이 + 고아 객체, 생명주기 >>
                1. CascadeType.ALL + orphanRemoval=true
                2. 스스로 생명 주기를 관리하는 엔티티는 em.persist()로 영속화, em.remove()로 제거
                3. 두 옵션을 모두 활성화하면 부모 엔티티를 통해서 자식의 생명 주기를 관리할 수 있음
                4. 도메인 주도 설계(DDD)의 Aggregate Root 개념을 구현할 때 유용
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

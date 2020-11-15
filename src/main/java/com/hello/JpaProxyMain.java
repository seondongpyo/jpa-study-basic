package com.hello;

import com.hello.entity.Member;
import com.hello.entity.Team;
import org.hibernate.Hibernate;
import org.hibernate.jpa.internal.PersistenceUnitUtilImpl;

import javax.persistence.*;

public class JpaProxyMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        try {
            Member member1 = new Member();
            member1.setUsername("member1");
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("member2");
            em.persist(member2);

            // '즉시 로딩'과 '지연 로딩'을 이해하기 위해서는 프록시(Proxy)의 동작 원리에 대해서 먼저 이해해야 한다!
            // em.find() vs. em.getReference()
            // 1. em.find() : 데이터베이스를 통해서 실제 엔티티 조회
//            Member m1 = em.find(Member.class, member1.getId());
//            Member m2 = em.find(Member.class, member2.getId());
//            System.out.println("findMember = " + m1.getClass()); // class com.hello.entity.Member
//            System.out.println("findMember.getId() = " + m1.getId());
//            System.out.println("findMember.getUsername() = " + m1.getUsername());
//            System.out.println("(m1 == m2) = " + (m1.getClass() == m2.getClass())); // true

            // 2. em.getReference() : 가짜(프록시) 엔티티 객체 조회
            Member m1 = em.find(Member.class, member1.getId());
            Member m2Ref = em.getReference(Member.class, member2.getId()); // select 쿼리가 호출되지 않음
            System.out.println("before reference = " + m2Ref.getClass()); // class com.hello.entity.Member$HibernateProxy$Xs3k7Nyl
            System.out.println("reference.getId() = " + m2Ref.getId()); // id 값을 이미 지정했으므로 select 쿼리가 호출되지 않음
            System.out.println("reference.getUsername() = " + m2Ref.getUsername()); // 실제 값을 조회할 때 select 쿼리가 호출됨
            System.out.println("after reference = " + m2Ref.getClass()); // class com.hello.entity.Member$HibernateProxy$Xs3k7Nyl
            // => 프록시 객체가 초기화된다고 해서 실제 엔티티로 바뀌는 것이 아니다!
            System.out.println("(m1.getClass() == m2Ref.getClass()) = " + (m1.getClass() == m2Ref.getClass())); // false

            // 찾으려고 하는 실제 엔티티가 이미 영속성 컨텍스트 안에 있으면 실제 엔티티가 반환된다.
            Member m1Ref = em.getReference((Member.class), member1.getId());
            System.out.println("m1Ref.getClass() = " + m1Ref.getClass()); // m1Ref.getClass() = class com.hello.entity.Member

            /*
                << 가짜(프록시) 객체 >>
                - 실제 클래스를 상속 받아서 만들어진다.
                - 실제 클래스와 겉모양이 똑같다.
                - 이론상 사용하는 입장에서는 조회한 객체가 진짜 객체인지 프록시 객체인지 구분하지 않고 사용하면 된다.
                - 프록시 객체는 실제 엔티티 객체의 참조(target)를 보관한다.
                - 프록시 객체를 호출하면 프록시 객체는 실제 엔티티 객체의 메서드를 호출한다.

                << 프록시 객체의 초기화 >>
                1. 클라이언트가 프록시 객체의 실제 값에 접근하려고 하면 (ex. proxy.getUsername())
                2. 프록시 객체 내부에 실제 Member를 참조하는 target이 없으므로 영속성 컨텍스트에 초기화를 요청한다.
                3. 영속성 컨텍스트에서 DB를 조회하여 실제 엔티티 객체를 생성하여 target과 연결시킨다.
                4. target.getUsername()이 호출되면서 실제 Member 엔티티 객체의 메서드인 member.getUsername()이 호출된다.

                << 프록시의 특징 >>
                1. 프록시 객체는 처음 사용할 때 한 번만 초기화된다.
                2. 프록시 객체를 초기화할 때, 프록시 객체가 실제 엔티티로 바뀌는 것이 아니다!
                   프록시 객체가 초기화되면 프록시 객체 내부의 참조에 의해 실제 엔티티에 접근이 가능한 것이다.
                3. 프록시 객체는 원본 엔티티를 상속해서 만들어지므로, 타입 체크 시 주의해야 한다! (== 대신 instance of 사용)
                4. 영속성 컨텍스트에 찾고자 하는 엔티티가 이미 있으면 em.getReference()를 호출해도 실제 엔티티가 반환된다.
                    => JPA는 같은 트랜잭션에서 영속성 컨텍스트의 동일성을 보장하기 때문에 PK가 같으면 항상 같은 인스턴스를 반환한다.
                       (하단의 (4. 추가) 내용 참조)
                5. 영속성 컨텍스트의 도움을 받을 수 없는 준영속(detached) 상태일 때 프록시를 초기화하면 예외가 발생한다
                    => 실무에서 많이 발생하는 예외
                       (하단의 (5. 추가) 내용 참조)
             */

            Member member3 = new Member();
            member3.setUsername("member3");
            em.persist(member3);

            em.flush();
            em.clear();

            // (4. 추가) 프록시 객체로 먼저 조회한 상태에서 실제 엔티티를 조회하려고 해도 프록시 객체가 조회된다.
            Member refMember = em.getReference(Member.class, member3.getId()); // Proxy
            Member findMember = em.find(Member.class, member3.getId()); // Member?
            System.out.println("refMember.getClass() = " + refMember.getClass()); // Proxy
            System.out.println("findMember.getClass() = " + findMember.getClass()); // (주의) Member가 아니라 Proxy!
            System.out.println("(refMember == findMember) = " + (refMember == findMember)); // true
            //  => JPA는 어떻게든 같은 트랜잭션에서 영속성 컨텍스트의 동일성을 보장하려고 하기 때문

            // 영속성 컨텍스트의 도움을 받을 수 없는 준영속(detached) 상태일 때 프록시를 초기화하면 예외가 발생한다
            Member member4 = new Member();
            member4.setUsername("member4");
            em.persist(member4);

            em.flush();
            em.clear();

            Member refMember4 = em.getReference(Member.class, member4.getId()); // 프록시 객체를 조회했는데
//            em.detach(refMember4);  // 준영속 상태라면
            System.out.println("refMember4.getUsername() = " + refMember4.getUsername()); // 프록시를 초기화할 때 예외가 발생한다.
            // org.hibernate.LazyInitializationException: could not initialize proxy [com.hello.entity.Member#4] - no Session

            // << 프록시 객체의 확인 >>
            // 1. 프록시 인스턴스의 초기화 여부 확인 (PersistenceUnitUtil.isLoaded())
            Member member5 = new Member();
            member5.setUsername("member5");
            em.persist(member5);

            em.flush();
            em.clear();

            Member refMember5 = em.getReference(Member.class, member5.getId());
            System.out.println("loaded = " + emf.getPersistenceUnitUtil().isLoaded(refMember5)); // 초기화 여부 확인 : false
            refMember5.getUsername(); // 초기화
            System.out.println("loaded = " + emf.getPersistenceUnitUtil().isLoaded(refMember5)); // 초기화 여부 확인 : true

            // 2. 프록시 클래스 확인 방법 (proxy.getClass().getName())
            Member member6 = new Member();
            member6.setUsername("member6");
            em.persist(member6);

            em.flush();
            em.clear();

            Member refMember6 = em.getReference(Member.class, member6.getId());
            System.out.println("refMember6.getClass().getName() = " + refMember6.getClass().getName());
            // => com.hello.entity.Member$HibernateProxy$yVRQ98WB

            // 3. 프록시 강제 초기화 (Hibernate에서 제공, JPA 표준은 강제 초기화가 없음)
            Member member7 = new Member();
            member7.setUsername("member7");
            em.persist(member7);

            em.flush();
            em.clear();

            Member refMember7 = em.getReference(Member.class, member7.getId());
            System.out.println("refMember7.getClass() = " + refMember7.getClass()); // Proxy
            Hibernate.initialize(refMember7); // Hibernate 프록시 강제 초기화 (select 쿼리가 호출됨)
            // => Hibernate가 아닌 다른 구현체인 경우에는 proxy.getUsername() 같은 메서드를 호출하여 강제로 초기화할 수 있다.

            transaction.commit();

        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();

        } finally {
            emf.close();
            em.close();
        }
    }

    private static void printMember(Member member) {
        System.out.println("member.getUsername() = " + member.getUsername());
    }

    private static void printMemberAndTeam(Member member) {
        String username = member.getUsername();
        System.out.println("username = " + username);

        Team team = member.getTeam();
        System.out.println("team.getName() = " + team.getName());
    }
}

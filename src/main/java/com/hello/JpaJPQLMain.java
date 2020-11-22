package com.hello;

import com.hello.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaJPQLMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        /*
            << SQL vs. JPQL >>
                - SQL : 데이터베이스 테이블을 대상으로 쿼리
                - JPQL : 테이블이 아닌, 엔티티 객체를 대상으로 쿼리

            << JPQL >>
                - JPA에서 제공하는, SQL을 추상화한 객체 지향 쿼리 언어
                - SQL과 문법이 유사하며, SELECT, FROM, WHERE, GROUP BY, HAVING, JOIN 등을 지원함
                - SQL을 추상화해서 특정 데이터베이스의 SQL에 의존하지 않음
                - 한 마디로 정의하면 객체 지향 SQL임
         */

        try {
            Member member = new Member();
            member.setUsername("hello");
            em.persist(member);

            // JPQL : 쿼리가 문자열이므로 동적 쿼리를 생성하기가 어렵다
            List<Member> memberList = em.createQuery("select m from Member m where m.username like '%hello%'", Member.class)
                    .getResultList();

            for (Member findMember : memberList) {
                System.out.println("findMember = " + findMember);
            }

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

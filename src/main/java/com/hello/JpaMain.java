package com.hello;

import com.hello.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        // !! 반드시 트랜잭션 안에서 처리해야 됨 !!
        try {
            // 엔티티 저장
//            Member member = new Member();
//            member.setId(1L);
//            member.setName("userA");
//            em.persist(member); // 엔티티를 데이터베이스에 저장

            // 엔티티 조회
//            Member member = em.find(Member.class, 1L);
//            System.out.println("member.getId() = " + member.getId());
//            System.out.println("member.getName() = " + member.getName());

            // 엔티티 수정
//            Member member = em.find(Member.class, 1L);
//            member.setName("userB");

            // 엔티티 삭제
//            Member member = em.find(Member.class, 1L);
//            em.remove(member);
//            Member findMember = em.find(Member.class, 1L);
//            System.out.println("findMember = " + findMember);   // findMember = null

            // JPQL 활용
            em.persist(new Member(0L, "memberA"));
            em.persist(new Member(1L, "memberB"));
            List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
            for (Member member : members) {
                System.out.println("member.getName() = " + member.getName());
            }

            transaction.commit();

        } catch (Exception e) {
            transaction.rollback();

        } finally {
            emf.close();
            em.close();
        }
    }
}

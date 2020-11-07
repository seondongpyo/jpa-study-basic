package com.hello;

import com.hello.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class PersistenceContextMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 새로운 엔티티 생성 (비영속)
            Member member = new Member(0L, "memberA");

            // 영속 (영속성 컨텍스트에서 해당 엔티티를 관리) = 1차 캐시에 저장
            System.out.println("=== BEFORE ===");
            em.persist(member); // 커밋이 호출되기 전까지는 실제로 데이터베이스에 저장되지 않는다!
            System.out.println("=== AFTER ===");

            // 1차 캐시에서 조회 (쿼리가 나가지 않음)
            System.out.println("=== BEFORE ===");
            Member memberA = em.find(Member.class, 0L);
            System.out.println("findMemberFromCache.getName() = " + memberA.getName());
            System.out.println("=== AFTER ===");

            // 1차 캐시에 없으면 데이터베이스에서 조회 (쿼리가 나감) 후 1차 캐시에 저장
            System.out.println("=== BEFORE ===");
            Member memberB1 = em.find(Member.class, 1L);
            System.out.println("findMemberFromDb.getName() = " + memberB1.getName());
            System.out.println("=== AFTER ===");

            // 1차 캐시에 저장되어 있으므로 재조회 시 쿼리가 나가지 않는다
            System.out.println("=== BEFORE ===");
            Member memberB2 = em.find(Member.class, 1L);
            System.out.println("findMember = " + memberB2);
            System.out.println("=== AFTER ===");

            // 영속 엔티티의 동일성 보장
            System.out.println("memberB1 == memberB2 : " + (memberB1 == memberB2));

            // 트랜잭션을 지원하는 쓰기 지연
            System.out.println("=== BEFORE COMMIT ===");
            tx.commit();    // 커밋이 호출되는 순간 쿼리가 나간다
            System.out.println("=== AFTER COMMIT ===");

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}

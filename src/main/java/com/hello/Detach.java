package com.hello;

import com.hello.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Detach {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 1차 캐시에 없는 엔티티 조회 -> 데이터베이스에서 엔티티 조회 -> 1차 캐시에 저장 -> 영속 상태
            Member member = em.find(Member.class, 10L);
            member.setName("memberAAA"); // 변경 감지를 통해 엔티티를 수정하려고 하는데

            em.detach(member); // 엔티티가 준영속 상태로 변경되면 영속성 컨텍스트가 해당 엔티티를 더 이상 관리하지 않으므로

            // 준영속 상태로 만드는 방법
//            em.detach(member); // 특정 엔티티만 준영속 상태로 변경
//            em.clear(); // 영속성 컨텍스트를 완전히 초기화
//            em.close(); // 영속성 컨텍스트를 종료

            tx.commit(); // 엔티티 변경이 실제로 일어나지 않는다 (= update 쿼리가 실행되지 않는다)

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}

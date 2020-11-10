package com.hello.concept;

import javax.persistence.*;

public class Flush {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
//            Member member = new Member(10L, "member10");
//            em.persist(member);

            // flush() 강제 호출
            // 플러시가 호출되면 영속성 컨텍스트의 쓰기 지연 SQL 저장소에 있는 쿼리들을 실제 데이터베이스에 반영한다
            // flush()를 호출하더라도 1차 캐시가 비워지지는 않는다
            em.flush();

            // flush 호출 모드
//            em.setFlushMode(FlushModeType.AUTO);    // 커밋 또는 JPQL 쿼리를 실행할 때 호출 (기본값)
//            em.setFlushMode(FlushModeType.COMMIT);  // 커밋할 때만 호출

            System.out.println("=================");

            // commit() : flush() 자동 호출
            // JPQL 쿼리 실행 시에도 flush()가 자동 호출된다.
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}

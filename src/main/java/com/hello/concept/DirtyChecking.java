package com.hello.concept;

import com.hello.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class DirtyChecking {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 엔티티 수정 : 변경 감지(Dirty Checking)
            Member member = em.find(Member.class, 0L);
            member.setName("memberC");

            tx.commit();

            /*
                commit()이 호출되면 내부적으로 flush()가 호출되는데 (flush : 영속성 컨텍스트의 변경 내용을 데이터베이스에 반영)
                이 때 영속성 컨텍스트에서 엔티티와 스냅샷을 비교한다.
                변경된 내용이 있다면 update 쿼리를 쓰기 지연 SQL 저장소에 저장한 뒤
                데이터베이스에서 flush(update 쿼리 실행)와 commit(변경된 사항을 확정)가 처리된다.
             */

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}

package com.hello;

import com.hello.entity.Member;
import com.hello.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaTestMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        try {
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("memberA");
            em.persist(member);

            /*
                양방향 연관관계 매핑 정리
                    - 단방향 매핑만으로도 이미 연관관계 매핑은 완료된 상태이다
                    - 양방향 매핑은 반대 방향으로 조회하는 기능이 추가된 것일 뿐이다
                    - JPQL에서 역방향으로 탐색할 일이 많다
                    - 단방향 매핑을 우선적으로 잘 하되, 양방향은 필요할 떄 추가하면 된다

                <주의>
                양방향 연관관계 매핑 시 toString, lombok, JSON 생성 라이브러리 등의
                사용으로 인한 무한루프를 조심하자
             */

            // 양방향 연관관계 매핑 시 순수 객체 상태를 고려하여 항상 양쪽에 값을 다 넣어주자
            // 또는 연관관계 편의 메서드를 생성하자
//            team.getMembers().add(member);
            team.addMember(member);

            em.flush();
            em.clear();

            // 객체 지향 모델링으로 수정
            Member findMember = em.find(Member.class, member.getId());
            Team findTeam = findMember.getTeam();

            // 양방향 연관관계 매핑
            List<Member> members = findTeam.getMembers();
            for (Member m : members) {
                System.out.println("m = " + m.getUsername());
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

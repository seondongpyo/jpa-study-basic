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
            member.setTeam(team);
            em.persist(member);

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

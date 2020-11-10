package com.hello;

import com.hello.entity.Member;
import com.hello.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

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
            member.setTeamId(team.getId());
            em.persist(member);

            // 객체 지향스럽지 않다...
            Member findMember = em.find(Member.class, member.getId());
            Team findTeam = em.find(Team.class, findMember.getId());

            transaction.commit();

        } catch (Exception e) {
            transaction.rollback();

        } finally {
            emf.close();
            em.close();
        }
    }
}

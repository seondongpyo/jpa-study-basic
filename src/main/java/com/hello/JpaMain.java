package com.hello;

import com.hello.entity.Member;
import com.hello.entity.Team;

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

        try {
            // '즉시 로딩'과 '지연 로딩'
            // 1. 지연 로딩 설정 시
            Team team1 = new Team();
            team1.setName("team1");
            em.persist(team1);

            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setTeam(team1);
            em.persist(member1);

            em.flush();
            em.clear();

            Member findMember1 = em.find(Member.class, member1.getId());
            /*
                select
                    member0_.MEMBER_ID as member_i1_4_0_,
                    member0_.CREATE_MEMBER as create_m2_4_0_,
                    member0_.createdDate as createdd3_4_0_,
                    member0_.UPDATE_MEMBER as update_m4_4_0_,
                    member0_.lastModifiedDate as lastmodi5_4_0_,
                    member0_.TEAM_ID as team_id7_4_0_,
                    member0_.USERNAME as username6_4_0_
                from
                    MEMBER_TEST member0_
                where
                    member0_.MEMBER_ID=?
             */
            // => Member 엔티티 조회 시, Team 엔티티 객체가 같이 조회되지 않았다!

            Team findTeam1 = findMember1.getTeam();
            System.out.println("findTeam1.getClass() = " + findTeam1.getClass()); // Proxy (class com.hello.entity.Team$HibernateProxy$MKONTkEt)

            // 실제 Team 엔티티의 !값을 사용하는 시점!에, Team 프록시 객체가 초기화(select 쿼리 호출)된다. (지연 로딩)
            System.out.println("findTeam1.getName() = " + findTeam1.getName());
            /*
                select
                    team0_.TEAM_ID as TEAM_ID1_8_0_,
                    team0_.CREATE_MEMBER as CREATE_M2_8_0_,
                    team0_.createdDate as createdD3_8_0_,
                    team0_.UPDATE_MEMBER as UPDATE_M4_8_0_,
                    team0_.lastModifiedDate as lastModi5_8_0_,
                    team0_.name as name6_8_0_
                from
                    Team team0_
                where
                    team0_.TEAM_ID=?
             */

            ////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////

            // 2. 즉시 로딩 설정 시 (ex. Member와 Team을 자주 함께 사용한다면?)
            Team team2 = new Team();
            team2.setName("team2");
            em.persist(team2);

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setTeam(team2);
            em.persist(member2);

            em.flush();
            em.clear();

            Member findMember2 = em.find(Member.class, member2.getId());
            /*
                select
                    member0_.MEMBER_ID as MEMBER_I1_4_0_,
                    member0_.CREATE_MEMBER as CREATE_M2_4_0_,
                    member0_.createdDate as createdD3_4_0_,
                    member0_.UPDATE_MEMBER as UPDATE_M4_4_0_,
                    member0_.lastModifiedDate as lastModi5_4_0_,
                    member0_.TEAM_ID as TEAM_ID7_4_0_,
                    member0_.USERNAME as USERNAME6_4_0_,
                    team1_.TEAM_ID as TEAM_ID1_8_1_,
                    team1_.CREATE_MEMBER as CREATE_M2_8_1_,
                    team1_.createdDate as createdD3_8_1_,
                    team1_.UPDATE_MEMBER as UPDATE_M4_8_1_,
                    team1_.lastModifiedDate as lastModi5_8_1_,
                    team1_.name as name6_8_1_
                from
                    Member member0_
                left outer join
                    Team team1_
                        on member0_.TEAM_ID=team1_.TEAM_ID
                where
                    member0_.MEMBER_ID=?
             */
            // => Member 엔티티 조회 시, Team 엔티티도 같이 조인하여 조회된다!

            Team findTeam2 = findMember2.getTeam();
            System.out.println("findTeam2.getClass() = " + findTeam2.getClass()); // Proxy가 아닌 실제 Team 엔티티! (class com.hello.entity.Team)
            System.out.println("findTeam2.getName() = " + findTeam2.getName()); // 이미 값이 채워져 있는 실제 Team 엔티티이므로 프록시처럼 select 쿼리가 호출되지 않는다.

            em.clear();

            ////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////

            /*
                << 프록시와 즉시 로딩 사용 시 주의 >>
                1. 실무에서는 가급적 지연 로딩만 사용해야 한다.
                    - 즉시 로딩 적용 시 예상하지 못한 SQL 쿼리가 호출된다!
                2. 즉시 로딩은 JPQL에서 N + 1 문제를 일으킨다.
                3. @ManyToOne, @OneToOne은 기본이 즉시 로딩이다. => 지연 로딩으로 설정 후 fetch 조인 활용
                4. @OneToMany, @ManyToMany는 기본이 지연 로딩이다.

                << 실무에서의 지연 로딩 활용 >>
                1. 모든 연관관계에 지연 로딩을 사용하라!
                2. 실무에서 즉시 로딩을 사용하지 마라!
                    => 대신 JPQL fetch 조인이나, 엔티티 그래프 기능을 사용해라!
                3. 즉시 로딩 사용 시 상상하지 못한 쿼리가 호출된다!
             */

            System.out.println("=========== JPQL 시작 ===========");
            List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
            for (Member member : members) {
                System.out.println("member.getUsername() = " + member.getUsername());
            }
            System.out.println("=========== JPQL 끝 ===========");

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

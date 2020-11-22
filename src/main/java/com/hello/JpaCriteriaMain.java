package com.hello;

import com.hello.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class JpaCriteriaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        /*
            << JPA Criteria >>
                - JPQL 빌더 역할
                - JPA에서 공식적으로 지원하는 기능
                - 장점 : 문자열이 아닌 자바 코드로 JPQL을 작성할 수 있음
                - 단점 : 너무 복잡하고 실용성이 없음
                    => Criteria 대신에 QueryDSL 사용을 권장함

            << QueryDSL >>
                - 오픈 소스 라이브러리
                - 문자가 아닌 자바 코드로 JPQL을 작성할 수 있음
                - JPQL 빌더 역할
                - 컴파일 시점에 문법 오류를 찾을 수 있음
                - 동적 쿼리 작성이 편리함
                - 단순하고 쉬우므로 실무에서의 사용을 권장

            << 네이티브 SQL >>
                - JPA가 제공하는, SQL을 직접 사용하는 기능
                - JPQL로 해결할 수 없는 특정 데이터베이스에 의존적인 기능을 활용할 때 사용
                    ex) 오라클의 CONNECT BY, 특정 데이터베이스만 사용하는 SQL 힌트 등

            << JDBC 직접 사용 또는 SpringJdbcTemplate 등 >>
                - JPA를 사용하면서 JDBC 커넥션을 직접 사용하거나, 스프링 JdbcTemplate, MyBatis 등을 함께 사용 가능
                - 단, 영속성 컨텍스트를 적절한 시점에 강제로 flush 해야 함
                    ex) JPA를 우회해서 SQL을 실행하기 직전에 영속성 컨텍스트를 수동으로 flush
         */

        try {
            Member member = new Member();
            member.setUsername("kim");
            em.persist(member);

            // Criteria 시작 =============================
            // Criteria 사용 준비
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Member> query = cb.createQuery(Member.class);

            // 루트 클래스 (조회를 시작할 클래스)
            Root<Member> m = query.from(Member.class);

            // Criteria 쿼리 생성 : 동적 쿼리를 생성하기가 편하지만, 복잡하므로 유지보수가 어렵다
            CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("username"), "kim"));
            List<Member> memberList = em.createQuery(cq).getResultList();

            for (Member findMember : memberList) {
                System.out.println("findMember.getUsername() = " + findMember.getUsername());
            }
            // Criteria 끝 =============================

            // 네이티브 SQL 시작 ============================
            List<Member> members = em.createNativeQuery("select MEMBER_ID, city, street, zipcode, username from Member where username like '%kim%'", Member.class)
                    .getResultList();

            for (Member findMember : members) {
                System.out.println("findMember = " + findMember);
            }
            // 네이티브 SQL 끝 --============================

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

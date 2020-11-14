package com.hello;

import com.hello.entity.Member;
import com.hello.entity.item.Movie;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        try {
            Member member = new Member();
            member.setUsername("userA");
            member.setCreatedBy("Kim");
            member.setCreatedDate(LocalDateTime.now());

            em.flush();
            em.clear();

            transaction.commit();

        } catch (Exception e) {
            transaction.rollback();

        } finally {
            emf.close();
            em.close();
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO2;

import Model2.ProductImages;
import Model2.ProductVariants;
import Model2.Products;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Lớp triển khai GenericDAO sử dụng JPA với persistence unit "Shop_Quan_Ao",
 * mở và đóng EntityManager tự động qua try-with-resources.
 *
 * @param <T> Kiểu entity
 * @param <K> Kiểu khóa chính (primary key)
 */
public class GenericDAOImpl<T, K> implements GenericDAO<T, K> {

    private static final EntityManagerFactory emf =
        Persistence.createEntityManagerFactory("Shop_Quan_Ao");

    private final Class<T> entityClass;

    public GenericDAOImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public void create(T entity) {
        // Mỗi lần create sẽ mở một EntityManager riêng và tự đóng
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                em.persist(entity);
                tx.commit();
            } catch (RuntimeException e) {
                if (tx.isActive()) tx.rollback();
                throw e;
            }
        }
    }

    @Override
    public T findById(K id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(entityClass, id);
        }
    }

    @Override
    public List<T> findAll() {
        try (EntityManager em = emf.createEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(entityClass);
            cq.from(entityClass);
            return em.createQuery(cq).getResultList();
        }
    }

    @Override
    public T update(T entity) {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                T merged = em.merge(entity);
                tx.commit();
                return merged;
            } catch (RuntimeException e) {
                if (tx.isActive()) tx.rollback();
                throw e;
            }
        }
    }

    @Override
    public void delete(T entity) {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                T managed = em.contains(entity) ? entity : em.merge(entity);
                em.remove(managed);
                tx.commit();
            } catch (RuntimeException e) {
                if (tx.isActive()) tx.rollback();
                throw e;
            }
        }
    }
    
}

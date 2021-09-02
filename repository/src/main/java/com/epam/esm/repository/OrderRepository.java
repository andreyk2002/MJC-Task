package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepository {

    private static final String FIND_ALL = "SELECT o FROM Order o";

    @PersistenceContext
    private EntityManager entityManager;


    public List<Order> getAll() {
        return entityManager.createQuery(FIND_ALL, Order.class).getResultList();
    }


    public Optional<Order> getById(long id) {
        Order order = entityManager.find(Order.class, id);
        return Optional.ofNullable(order);
    }


    @Transactional
    public Order addOrder(Order order) {
        entityManager.persist(order);
        return order;
    }

    public List<Order> getPage(int size, int offset) {
        return entityManager.createQuery(FIND_ALL, Order.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
    }
}

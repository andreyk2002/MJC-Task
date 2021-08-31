package com.epam.esm.repository.impl;

import com.epam.esm.entity.Order;
import com.epam.esm.repository.OrderRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaOrderRepository implements OrderRepository {

    private static final String FIND_ALL = "SELECT o FROM Order o";
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Order> getAll() {
        return entityManager.createQuery(FIND_ALL).getResultList();
    }

    @Override
    public Optional<Order> getById(long id) {
        Order order = entityManager.find(Order.class, id);
        return Optional.ofNullable(order);
    }
}

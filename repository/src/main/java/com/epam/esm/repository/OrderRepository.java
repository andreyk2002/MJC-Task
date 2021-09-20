package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;


/**
 * UserRepository provides functionality for interaction with storage,
 * which contains data about {@link User} entities
 */


@Repository
public class OrderRepository {

    private static final String FIND_ALL = "SELECT o FROM Order o";

    @PersistenceContext
    private EntityManager entityManager;


    /**
     * Searches order by specified id
     *
     * @param id - ID of the order, needed to be find
     * @return User with specified id if order with specified id exists in the storage, e
     * else returns Optional.empty()
     */
    public Optional<Order> getById(long id) {
        Order order = entityManager.find(Order.class, id);
        return Optional.ofNullable(order);
    }


    /**
     * Adds new order to the storage
     *
     * @param order order needed to be saved in the storage
     * @return inserted order
     */
    public Order addOrder(Order order) {

        entityManager.persist(order);
        return order;
    }

    /**
     * Return a page of orders within specified range
     *
     * @param size   -  maximal number of orders in one page
     * @param offset - number of order from which page starts
     * @return List of all orders located within specified range
     */
    public List<Order> getPage(int size, int offset) {
        return entityManager.createQuery(FIND_ALL, Order.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
    }
}

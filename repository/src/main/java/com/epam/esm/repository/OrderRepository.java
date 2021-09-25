package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * UserRepository provides functionality for interaction with storage,
 * which contains data about {@link User} entities
 */


public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByUserLogin(Pageable pageable, String login);
}

package com.epam.esm.repository;

import com.epam.esm.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    List<Order> getAll();

    Optional<Order> getById(long id);
}

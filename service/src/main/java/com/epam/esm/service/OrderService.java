package com.epam.esm.service;


import com.epam.esm.entity.Order;
import com.epam.esm.mappers.OrderMapper;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.response.OrderResponseDto;
import com.epam.esm.service.excepiton.OrderNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Provides set of operations with {@link Order} entities
 */

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderMapper mapper;
    private final OrderRepository orderRepository;

    /**
     * Searches order by specified id
     *
     * @param id - ID of the order to be find
     * @return instance of order with specified id
     * @throws OrderNotFoundException if order with specified id not present in repository
     */
    public OrderResponseDto getById(long id) {
        Optional<Order> optionalOrder = orderRepository.getById(id);
        return optionalOrder.map(mapper::entityToResponse)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    /**
     * Return a page of orders within specified range
     *
     * @param size   -  maximal number of orders in one page
     * @param offset - number of order from which page starts
     * @return List of all orders located within specified range
     */
    public List<OrderResponseDto> getPage(int size, int offset) {
        List<Order> page = orderRepository.getPage(size, offset);
        return mapper.entitiesToResponse(page);
    }
}

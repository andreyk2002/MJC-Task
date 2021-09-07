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

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderMapper mapper;
    private final OrderRepository orderRepository;

    public List<OrderResponseDto> getAll() {
        List<Order> all = orderRepository.getAll();
        return mapper.entitiesToResponse(all);
    }

    public OrderResponseDto getById(long id) {
        Optional<Order> optionalOrder = orderRepository.getById(id);
        return optionalOrder.map(mapper::entityToResponse)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }


    public List<OrderResponseDto> getPage(int size, int offset) {
        List<Order> page = orderRepository.getPage(size, offset);
        return mapper.entitiesToResponse(page);
    }
}

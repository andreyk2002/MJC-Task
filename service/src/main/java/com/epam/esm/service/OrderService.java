package com.epam.esm.service;


import com.epam.esm.UserRole;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.mappers.OrderMapper;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.response.OrderResponseDto;
import com.epam.esm.service.excepiton.OrderNotFoundException;
import com.epam.esm.service.excepiton.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final UserRepository userRepository;

    /**
     * Searches order by specified id
     *
     * @param id - ID of the order to be finded
     * @return instance of order with specified id
     * @throws OrderNotFoundException if order with specified id not present in repository
     */
    public OrderResponseDto getById(long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        return optionalOrder.map(mapper::entityToResponse)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    /**
     * Return a page of orders within specified range
     *
     * @param pageable - specifies the page number and page size
     * @return List of all orders located within specified range
     */
    public List<OrderResponseDto> getPage(Pageable pageable, String login) {
        Page<Order> page;
        Optional<User> user = userRepository.findByLogin(login);
        UserRole role = user.map(User::getRole).orElseThrow(UserNotFoundException::new);
        if (role == UserRole.ADMIN) {
            page = orderRepository.findAll(pageable);
        } else {
            page = orderRepository.findAllByUserLogin(pageable, login);
        }
        return mapper.entitiesToResponse(page.toList());
    }
}

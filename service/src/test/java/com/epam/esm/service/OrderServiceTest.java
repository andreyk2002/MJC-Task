package com.epam.esm.service;

import com.epam.esm.entity.Order;
import com.epam.esm.mappers.OrderMapper;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.response.OrderResponseDto;
import com.epam.esm.service.excepiton.OrderNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderRepository orderRepository;


    @Test
    void testGetAllShouldReturnAllOrders() {
        int size = 2;
        int offset = 0;
        OrderResponseDto firstOrderResponse = OrderResponseDto.builder().id(1).build();
        OrderResponseDto secondOrderResponse = OrderResponseDto.builder().id(2).build();
        List<OrderResponseDto> expectedOrders = Arrays.asList(firstOrderResponse, secondOrderResponse);
        Order firstOrder = Order.builder().id(1).build();
        Order secondOrder = Order.builder().id(2).build();
        List<Order> orders = Arrays.asList(firstOrder, secondOrder);

        when(orderRepository.getPage(anyInt(), anyInt())).thenReturn(orders);
        when(orderMapper.entitiesToResponse(anyList())).thenReturn(expectedOrders);

        List<OrderResponseDto> all = orderService.getPage(size, offset);

        Assertions.assertEquals(expectedOrders, all);
        verify(orderRepository).getPage(size, offset);
        verify(orderMapper).entitiesToResponse(orders);
    }

    @Test
    void testGetByIdShouldReturnOrderWhenExists() {
        int existingId = 1;
        Order order = Order.builder().id(existingId).build();
        OrderResponseDto expectedOrderResponse = OrderResponseDto.builder().id(existingId).build();
        when(orderRepository.getById(anyLong())).thenReturn(Optional.of(order));
        when(orderMapper.entityToResponse(any())).thenReturn(expectedOrderResponse);
        OrderResponseDto resultOrder = orderService.getById(existingId);

        Assertions.assertEquals(expectedOrderResponse, resultOrder);

        verify(orderRepository).getById(existingId);
        verify(orderMapper).entityToResponse(order);
    }

    @Test
    void testGetByIdShouldThrowWhenNotExists() {
        int notExistingId = 4242424;
        when(orderRepository.getById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.getById(notExistingId));

        verify(orderRepository).getById(notExistingId);
    }
}
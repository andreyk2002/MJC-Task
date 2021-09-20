package com.epam.esm.mappers;

import com.epam.esm.entity.Order;
import com.epam.esm.response.OrderResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class OrderMapperTest {

    @InjectMocks
    private final OrderMapper orderMapper = new OrderMapperImpl();

    @Mock
    private CertificateMapper certificateMapper;

    @Mock
    private UserMapper userMapper;


    @Test
    void testEntityToResponseShouldTransformEntityToResponse() {
        Order order = Order.builder()
                .id(1)
                .orderPrice(new BigDecimal("200"))
                .build();
        OrderResponseDto expected = OrderResponseDto.builder()
                .id(1)
                .orderPrice(new BigDecimal("200"))
                .build();

        OrderResponseDto result = orderMapper.entityToResponse(order);
        Assertions.assertEquals(result, expected);
    }

    @Test
    void testEntityToResponseShouldReturnNullWhenEntityIsNull() {
        OrderResponseDto result = orderMapper.entityToResponse(null);
        Assertions.assertNull(result);
    }


    @Test
    void entitiesToResponsesShouldParseEntitiesToResponses() {
        Order firstOrder = Order.builder()
                .id(1)
                .orderPrice(new BigDecimal("200"))
                .build();
        Order secondOrder = Order.builder()
                .id(2)
                .orderPrice(new BigDecimal("500"))
                .build();
        OrderResponseDto firstOrderResponse = OrderResponseDto.builder()
                .id(1)
                .orderPrice(new BigDecimal("200"))
                .build();
        OrderResponseDto secondOrderResponse = OrderResponseDto.builder()
                .id(1)
                .orderPrice(new BigDecimal("500"))
                .build();
        List<Order> orders = Arrays.asList(firstOrder, secondOrder);
        List<OrderResponseDto> expected = Arrays.asList(firstOrderResponse, secondOrderResponse);
        List<OrderResponseDto> result = orderMapper.entitiesToResponse(orders);
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testEntityToResponseShouldReturnNullWhenEntitiesIsNull() {
        List<OrderResponseDto> result = orderMapper.entitiesToResponse(null);
        Assertions.assertNull(result);
    }


}
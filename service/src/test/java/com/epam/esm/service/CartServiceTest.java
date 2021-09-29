package com.epam.esm.service;

import com.epam.esm.UserRole;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.mappers.OrderMapper;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.response.CertificateResponseDto;
import com.epam.esm.response.OrderResponseDto;
import com.epam.esm.response.UserResponseDto;
import com.epam.esm.service.excepiton.UserNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private CertificateRepository certificateRepository;


    @Test
    void createOrderShouldCreateOrder() {
        long userId = 10;
        GiftCertificate firstCertificate = GiftCertificate.builder()
                .id(4).price(new BigDecimal("1")).build();
        GiftCertificate secondCertificate = GiftCertificate.builder()
                .id(6).price(new BigDecimal("2")).build();
        List<GiftCertificate> certificates = Arrays.asList(firstCertificate, secondCertificate);
        CertificateResponseDto firstCertificateResponse = CertificateResponseDto.builder()
                .id(4).price(new BigDecimal("1")).build();
        CertificateResponseDto secondCertificateResponse = CertificateResponseDto
                .builder().id(6).price(new BigDecimal("2")).build();
        List<CertificateResponseDto> responses = Arrays.asList(firstCertificateResponse, secondCertificateResponse);
        List<Long> certificateIds = Arrays.asList(4L, 6L);
        String userLogin = "login";
        User user = User.builder().id(userId).name("name").login(userLogin).role(UserRole.USER).build();
        UserResponseDto userResponse = new UserResponseDto(userId, "name", UserRole.USER);

        OrderResponseDto expectedOrder = OrderResponseDto.builder()
                .id(1)
                .user(userResponse)
                .orderPrice(new BigDecimal("3"))
                .certificates(responses)
                .build();
        Order order = Order.builder()
                .id(0)
                .user(user)
                .orderPrice(new BigDecimal("3"))
                .certificates(certificates)
                .build();

        when(certificateRepository.findAllInIdRange(anyList())).thenReturn(certificates);
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(user));
        when(orderRepository.save(any())).thenReturn(order);
        when(orderMapper.entityToResponse(any())).thenReturn(expectedOrder);

        OrderResponseDto createdOrder = cartService.createOrder(userLogin, certificateIds);

        Assertions.assertEquals(expectedOrder, createdOrder);

        verify(userRepository).findByLogin(userLogin);
        verify(certificateRepository).findAllInIdRange(eq(certificateIds));
        verify(orderRepository).save(eq(order));
        verify(orderMapper).entityToResponse(order);
    }

    @Test
    void createOrderShouldThrowWhenUserNotFound() {
        String notExistingLogin = "login";
        when(userRepository.findByLogin(notExistingLogin)).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class,
                () -> cartService.createOrder(notExistingLogin, Collections.emptyList()));
        verify(userRepository).findByLogin(notExistingLogin);
    }
}
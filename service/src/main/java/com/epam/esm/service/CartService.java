package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.mappers.CertificateMapper;
import com.epam.esm.mappers.OrderMapper;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.response.OrderResponseDto;
import com.epam.esm.service.excepiton.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CartService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CertificateMapper certificateMapper;
    private final OrderMapper orderMapper;
    private final CertificateRepository certificateRepository;


    public OrderResponseDto createOrder(long userId, List<Integer> certificates) {
        Optional<User> optionalUser = userRepository.getById(userId);
        List<Integer> certificateRange = certificates.stream().sorted().distinct().collect(toList());
        List<GiftCertificate> giftCertificates = certificateRepository.findInRange(certificateRange);
        BigDecimal totalPrice = giftCertificates.stream()
                .map(GiftCertificate::getPrice)
                .reduce(new BigDecimal("0"), BigDecimal::add);
        return optionalUser.map(user -> {
            Order order = Order.builder()
                    .user(user)
                    .orderPrice(totalPrice)
                    .certificates(giftCertificates)
                    .build();
            Order addedOrder = orderRepository.addOrder(order);
            return orderMapper.entityToResponse(addedOrder);
        }).orElseThrow(() -> new UserNotFoundException(userId));
    }


}

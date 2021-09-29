package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.mappers.OrderMapper;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.response.OrderResponseDto;
import com.epam.esm.service.excepiton.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * provides opportunities about managing {@link Order} entities
 */

@Service
@AllArgsConstructor
public class CartService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CertificateRepository certificateRepository;


    /**
     * Creates order for user with specified id
     *
     * @param userLogin    - login of user who makes order
     * @param certificates - ids of certificates which included to order
     * @return instance of created order
     * @throws UserNotFoundException is user with specified id not exists
     */
    @Transactional
    public OrderResponseDto createOrder(String userLogin, List<Long> certificates) {
        Optional<User> optionalUser = userRepository.findByLogin(userLogin);
        List<Long> certificateRange = certificates.stream().sorted().distinct().collect(toList());
        List<GiftCertificate> giftCertificates = certificateRepository.findAllInIdRange(certificateRange);
        BigDecimal totalPrice = giftCertificates.stream()
                .map(GiftCertificate::getPrice)
                .reduce(new BigDecimal("0"), BigDecimal::add);
        return optionalUser.map(user -> {
            Order order = Order.builder()
                    .user(user)
                    .orderPrice(totalPrice)
                    .certificates(giftCertificates)
                    .build();
            Order addedOrder = orderRepository.save(order);
            return orderMapper.entityToResponse(addedOrder);
        }).orElseThrow(() -> new UserNotFoundException(userLogin));
    }


}

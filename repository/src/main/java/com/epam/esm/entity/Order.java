package com.epam.esm.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", updatable = false, nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "certificate_id", referencedColumnName = "id", updatable = false, nullable = false)
    private GiftCertificate giftCertificate;

    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;

    @Column(name = "order_price")
    private BigDecimal orderPrice;
}

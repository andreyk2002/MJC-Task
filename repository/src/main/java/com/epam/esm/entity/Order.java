package com.epam.esm.entity;


import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", updatable = false, nullable = false)
    private User user;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.ALL})
    @JoinTable(
            name = "order_certificate",
            joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "certificate_id", referencedColumnName = "id")
    )
    private List<GiftCertificate> certificates;

    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;

    @Column(name = "order_price")
    private BigDecimal orderPrice;

    @PrePersist
    public void prePersist() {
        purchaseDate = LocalDateTime.now();
    }


}

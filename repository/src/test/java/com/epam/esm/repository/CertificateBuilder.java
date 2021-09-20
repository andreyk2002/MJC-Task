package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

class CertificateBuilder {
    public GiftCertificate buildCertificate(long id, String name) {

        return GiftCertificate.builder()
                .name(name)
                .id(id)
                .price(new BigDecimal("19.00"))
                .tags(Collections.emptySet())
                .orders(Collections.emptySet())
                .duration(0)
                .createDate(LocalDateTime.of(2002, 8, 12, 3, 11))
                .lastUpdateDate(LocalDateTime.of(2005, 8, 12, 3, 11))
                .build();
    }

    public GiftCertificate buildCertificate(String name) {
        return buildCertificate(0, name);
    }
}

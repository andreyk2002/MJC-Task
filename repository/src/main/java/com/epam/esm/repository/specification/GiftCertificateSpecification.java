package com.epam.esm.repository.specification;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class GiftCertificateSpecification {

    public Specification<GiftCertificate> getCertificates() {
        return null;
    }
}

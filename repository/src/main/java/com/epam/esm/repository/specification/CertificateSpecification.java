package com.epam.esm.repository.specification;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftTag;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import java.util.Objects;

public class CertificateSpecification implements Specification<GiftCertificate> {
    private final String tagName;
    private final String keywordLike;

    public CertificateSpecification(String tagName, String keyword) {
        this.tagName = tagName;
        this.keywordLike = "%" + keyword + "%";
    }

    @Override
    public Predicate toPredicate(Root<GiftCertificate> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        EntityType<GiftCertificate> certificateEntityType = root.getModel();
        Join<GiftCertificate, GiftTag> tasks = root.join(
                certificateEntityType.getSet("tags", GiftTag.class), JoinType.LEFT);
        Predicate byKeyword = criteriaBuilder.or(
                criteriaBuilder.like(root.get("name"), keywordLike),
                criteriaBuilder.like(root.get("description"), keywordLike)
        );
        if (tagName != null) {
            String tagNameLike = "%" + tagName + "%";
            return criteriaBuilder.and(
                    byKeyword,
                    criteriaBuilder.like(tasks.get("name"), tagNameLike)
            );
        }
        return byKeyword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CertificateSpecification)) return false;
        CertificateSpecification that = (CertificateSpecification) o;
        return Objects.equals(tagName, that.tagName) && Objects.equals(keywordLike, that.keywordLike);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagName, keywordLike);
    }
}

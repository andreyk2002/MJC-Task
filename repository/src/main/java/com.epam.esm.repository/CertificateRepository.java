package com.epam.esm.repository;


import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * CertificateRepository provides functionality for interaction with storage,
 * which contains data about {@link GiftCertificate} entities
 */


public interface CertificateRepository extends JpaRepository<GiftCertificate, Long>,
        JpaSpecificationExecutor<GiftCertificate> {


    @Query("select gc from GiftCertificate gc where gc.id IN (:ids)")
    List<GiftCertificate> findAllInIdRange(@Param("ids") List<Long> certificatesIds);

    @Query("select gc from GiftCertificate gc JOIN gc.tags t WHERE t.id IN (:tagIds) " +
            "group by gc.id having count(t.id) = (:count)")
    List<GiftCertificate> findByTags(@Param("tagIds") List<Long> tagIds, @Param("count") long count);

    //TODO: can we implement it better
    default List<GiftCertificate> findByTags(@Param("tagIds") List<Long> tagIds) {
        return findByTags(tagIds, tagIds.size());
    }

}

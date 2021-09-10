package com.epam.esm.repository;


import com.epam.esm.entity.GiftCertificate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

/**
 * CertificateRepository provides functionality for interaction with storage,
 * which contains data about {@link GiftCertificate} entities
 */


@Repository
@AllArgsConstructor
public class CertificateRepository {

    private static final String FIND_ALL = "SELECT c FROM GiftCertificate c";


    @PersistenceContext
    private final EntityManager entityManager;

    /**
     * Adds an instance of {@link GiftCertificate} into the storage
     *
     * @param giftCertificate instance of certificate, needed to be added
     * @return ID of inserted certificate
     */
    @Transactional
    public GiftCertificate addCertificate(GiftCertificate giftCertificate) {
        entityManager.persist(giftCertificate);
        entityManager.flush();
        return giftCertificate;
    }


    /**
     * Removes a certificate with specified id from storage if present
     *
     * @param id - ID of certificate to be removed
     */
    public void deleteById(long id) {
        GiftCertificate certificate = entityManager.find(GiftCertificate.class, id);
        entityManager.remove(certificate);
    }

    /**
     * Updates an instance of {@link GiftCertificate} in the storage
     *
     * @param giftCertificate instance of certificate, needed to be updated
     * @return entity of updated certificate
     */
    @Transactional
    public GiftCertificate updateCertificate(GiftCertificate giftCertificate) {
        GiftCertificate updated = entityManager.merge(giftCertificate);
        entityManager.flush();
        return updated;
    }

    /**
     * Get list of all certificates, which are present in the storage
     *
     * @return List of all present certificates
     */
    public List<GiftCertificate> getPage(int offset, int size) {
        return entityManager.createQuery(FIND_ALL, GiftCertificate.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
    }

    /**
     * Searches for certificate with specified id in the storage
     *
     * @param id - ID of certificate
     * @return instance of GiftCertificate  wrapped with {@link Optional} if present,
     * else returns {@link Optional#empty()}
     */
    public Optional<GiftCertificate> getById(long id) {
        GiftCertificate certificate = entityManager.find(GiftCertificate.class, id);
        return Optional.ofNullable(certificate);
    }

    /**
     * Searches list of all certificates depends on keyword (part of name or description) or/and tag name
     * in specified order
     *
     * @param filter stores information about which criterias of selecting certificates.
     *               May include tagName -  name of the tag which certificate should contain,
     *               keyword - part of the name or description which certificate should have
     *               sortOrder -  type of sort order (ascending, descending),
     *               sortField - name of the field by which certificates should be ordered
     * @return List of certificates which applied to the mentioned criterias
     */


    //TODO : criteria api last time
    public List<GiftCertificate> getAllSorted(CertificateFilter filter) {
        StringBuilder query = new StringBuilder("SELECT DISTINCT gc FROM GiftCertificate gc JOIN gc.tags t " +
                "WHERE (?1 is NULL  OR t.name LIKE concat('%', ?1, '%')) AND " +
                "(?2 is NULL OR gc.name LIKE concat('%', ?2, '%') OR gc.description LIKE concat('%', ?2, '%')) " +
                "ORDER BY ");
        query.append("gc.");
        query.append(filter.getSortField());
        query.append(" ");
        query.append(filter.getSortOrder());
        return entityManager.createQuery(query.toString(), GiftCertificate.class)
                .setParameter(1, filter.getTagName())
                .setParameter(2, filter.getKeyword())
                .setFirstResult(filter.getOffset())
                .setMaxResults(filter.getPageSize())
                .getResultList();
    }

    public List<GiftCertificate> findInRange(List<Long> certificatesIds) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> query = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> certificateRoot = query.from(GiftCertificate.class);
        query.select(certificateRoot).where(certificateRoot.in(certificatesIds));
        return entityManager
                .createQuery(query)
                .getResultList();
    }


    public List<GiftCertificate> findByTags(List<Long> tagIds) {
        TypedQuery<GiftCertificate> query = entityManager.createQuery(
                "select gc from GiftCertificate gc JOIN gc.tags t" +
                        " WHERE t.id IN ?1" +
                        " group by gc.id having count(t.id) = ?2", GiftCertificate.class);
        query.setParameter(1, tagIds);
        query.setParameter(2, (long) tagIds.size());
        return query.getResultList();
    }
}

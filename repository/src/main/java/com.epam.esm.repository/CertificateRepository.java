package com.epam.esm.repository;


import com.epam.esm.entity.GiftCertificate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    public GiftCertificate updateCertificate(GiftCertificate giftCertificate) {
        return entityManager.merge(giftCertificate);
    }

    /**
     * Get list of all certificates, which are present in the storage
     *
     * @return List of all present certificates
     */
    public List<GiftCertificate> getAll() {
        return entityManager.createQuery(FIND_ALL, GiftCertificate.class).getResultList();
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
     * @param keyword   part of name or description which certificates should contain. In case if keyword is null
     *                  all certificates are specified to this criteria
     * @param tagName   name of the tag which certificate should contain. In case if keyword is null
     *                  all certificates are specified to this criteria
     * @param sortOrder type of sort order (ascending, descending)
     * @param sortField name of the field by which certificates should be ordered
     * @return List of certificates which applied to the mentioned criterias
     */

    //TODO: return filtering
    public List<GiftCertificate> getAllSorted(String keyword, String tagName, String sortOrder, String sortField) {
        return entityManager.createQuery(FIND_ALL, GiftCertificate.class).getResultList();
    }

    public List<GiftCertificate> findInRange(List<Integer> certificatesIds) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> query = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> certificateRoot = query.from(GiftCertificate.class);
        query.select(certificateRoot).where(certificateRoot.in(certificatesIds));
        return entityManager
                .createQuery(query)
                .getResultList();
    }
}

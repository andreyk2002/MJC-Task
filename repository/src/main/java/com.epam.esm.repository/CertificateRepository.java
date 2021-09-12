package com.epam.esm.repository;


import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftTag;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
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
     * @return instance of updated certificate
     */
    @Transactional
    public GiftCertificate updateCertificate(GiftCertificate giftCertificate) {
        GiftCertificate updated = entityManager.merge(giftCertificate);
        entityManager.flush();
        return updated;
    }

    /**
     * Return a page of certificates within specified range
     *
     * @param size   -  maximal number of certificates in one page
     * @param offset - number of user from which page starts
     * @return List of all certificates located within specified range
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


    public List<GiftCertificate> getAllSorted(CertificateFilter filter) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> certificateRoot = criteriaQuery.from(GiftCertificate.class);
        Metamodel metamodel = entityManager.getMetamodel();
        EntityType<GiftCertificate> certificateEntityType = metamodel.entity(GiftCertificate.class);
        Join<GiftCertificate, GiftTag> tasks = certificateRoot.join(
                certificateEntityType.getSet("tags", GiftTag.class), JoinType.LEFT);
        String keyword = "%" + filter.getKeyword() + "%";
        String sortField = filter.getSortField();
        String sortOrder = filter.getSortOrder();
        Order order;
        Path<Object> sort = certificateRoot.get(sortField);
        if (sortOrder.equalsIgnoreCase("asc")) {
            order = criteriaBuilder.asc(sort);
        } else {
            order = criteriaBuilder.desc(sort);
        }
        String tagName = filter.getTagName();
        Predicate findInCertificates = criteriaBuilder.or(
                criteriaBuilder.like(certificateRoot.get("name"), keyword),
                criteriaBuilder.like(certificateRoot.get("description"), keyword)
        );
        Predicate searchPredicate = findInCertificates;
        if (tagName != null) {
            searchPredicate = criteriaBuilder.and(
                    findInCertificates,
                    criteriaBuilder.equal(tasks.get("name"), tagName)
            );
        }
        criteriaQuery.select(certificateRoot)
                .distinct(true)
                .where(searchPredicate)
                .orderBy(order);
        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(filter.getOffset())
                .setMaxResults(filter.getPageSize())
                .getResultList();
    }

    /**
     * Searches certificates which ids are present in specified list
     *
     * @param certificatesIds list of needed certificates ids
     * @return all certificates, which ids are present in specified list
     */
    public List<GiftCertificate> findInRange(List<Long> certificatesIds) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> query = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> certificateRoot = query.from(GiftCertificate.class);
        query.select(certificateRoot).where(certificateRoot.in(certificatesIds));
        return entityManager
                .createQuery(query)
                .getResultList();
    }


    /**
     * Searches certificates which tags ids are present in specified list
     *
     * @param tagIds ids all of which certificates should contain
     * @return all certificates, which contain all needed tags
     */
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

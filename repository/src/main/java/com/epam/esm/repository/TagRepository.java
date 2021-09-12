package com.epam.esm.repository;

import com.epam.esm.entity.GiftTag;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@NoArgsConstructor
@AllArgsConstructor
public class TagRepository {

    private static final String FIND_ALL = "SELECT t FROM GiftTag t";

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Gets the most widely used id of user with the highest total sum of orders
     *
     * @return most widely used id of user with the highest total sum of orders
     */
    public GiftTag getTopUserTopTag() {
        return (GiftTag) entityManager.createNativeQuery(
                        "select t.name AS name, ct.tag_id AS id, oc.certificate_id, oc.order_id, count(tag_id) \n" +
                                "FROM tag t\n" +
                                " JOIN certificate_tag ct\n" +
                                " ON t.id = ct.id\n" +
                                " JOIN order_certificate oc\n" +
                                " ON ct.certificate_id = oc.certificate_id\n" +
                                " JOIN user_order uo \n" +
                                "ON uo.id = oc.order_id\n" +
                                "where uo.user_id = (\n" +
                                "select uo.user_id from user_order uo\n" +
                                "group by uo.user_id order by sum(uo.order_price) desc limit 1\n" +
                                ") group by tag_id order by count(tag_id) desc limit 1", GiftTag.class)
                .getSingleResult();
    }


    /**
     * Adds new tag to the storage
     *
     * @param tag order needed to be saved in the storage
     * @return inserted tag
     */
    @Transactional
    public GiftTag addTag(GiftTag tag) {
        entityManager.persist(tag);
        entityManager.flush();
        return tag;
    }


    /**
     * Updates an instance of {@link GiftTag} in the storage
     *
     * @param giftTag instance of certificate, needed to be updated
     */
    @Transactional
    public void updateTag(GiftTag giftTag) {
        entityManager.merge(giftTag);
        entityManager.flush();
    }


    /**
     * Removes a tag with specified id from storage if present
     *
     * @param tagId - ID of certificate to be removed
     */
    public void deleteById(long tagId) {
        GiftTag giftTag = entityManager.find(GiftTag.class, tagId);
        entityManager.remove(giftTag);
    }

    /**
     * Searches for tag with specified id in the storage
     *
     * @param id - ID of tag
     * @return instance of GiftTag  wrapped with {@link Optional} if present,
     * else returns {@link Optional#empty()}
     */
    public Optional<GiftTag> getById(long id) {
        GiftTag giftTag = entityManager.find(GiftTag.class, id);
        return Optional.ofNullable(giftTag);
    }


    /**
     * Return a page of tags within specified range
     *
     * @param size   -  maximal number of tags in one page
     * @param offset - number of tags from which page starts
     * @return List of all tags located within specified range
     */
    public List<GiftTag> getPage(int offset, int size) {
        return entityManager.createQuery(FIND_ALL, GiftTag.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
    }
}

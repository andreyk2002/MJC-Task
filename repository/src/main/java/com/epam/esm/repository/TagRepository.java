package com.epam.esm.repository;

import com.epam.esm.entity.GiftTag;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class TagRepository {

    private static final String FIND_ALL = "SELECT t FROM GiftTag t";
    @PersistenceContext
    private EntityManager entityManager;

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
                        ") group by tag_id order by count(tag_id) desc limit 1", GiftTag.class).getSingleResult();
    }


    @Transactional
    public GiftTag addTag(GiftTag tag) {
        entityManager.persist(tag);
        entityManager.flush();
        return tag;
    }


    @Transactional
    public void updateTag(GiftTag giftTag) {
        entityManager.merge(giftTag);
    }


    public void deleteById(long tagId) {
        GiftTag giftTag = entityManager.find(GiftTag.class, tagId);
        entityManager.remove(giftTag);
    }


    public Optional<GiftTag> getById(long id) {
        GiftTag giftTag = entityManager.find(GiftTag.class, id);
        return Optional.ofNullable(giftTag);
    }


    public List<GiftTag> getAll() {
        return entityManager.createQuery(FIND_ALL, GiftTag.class).getResultList();
    }

    public List<GiftTag> getPage(int offset, int size) {
        return entityManager.createQuery("SELECT t FROM GiftTag t", GiftTag.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
    }
}

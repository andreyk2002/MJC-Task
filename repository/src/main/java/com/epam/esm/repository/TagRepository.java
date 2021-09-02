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
        return (GiftTag) entityManager.createQuery(" SELECT t.name AS name, t.id AS id, count(t.id) AS count" +
                " from tag t" +
                " JOIN  certificate_tag ct" +
                " on t.id = ct.tag_id" +
                " where ct.certificate_id IN (SELECT certificate_id FROM user_order" +
                " WHERE user_id = (SELECT user_id FROM user_order " +
                " group by user_id" +
                " order by sum(order_price) desc" +
                " limit 1)" +
                ")" +
                " group by t.id" +
                " ORDER By count(t.id) desc" +
                " limit 1").getSingleResult();
    }


    @Transactional
    public GiftTag addTag(GiftTag tag) {
        entityManager.persist(tag);
//        entityManager.flush();
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

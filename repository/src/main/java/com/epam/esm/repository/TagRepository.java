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
        Long userId = entityManager.createQuery("SELECT ord.user.id FROM Order ord" +
                " GROUP BY ord.user.id order by sum (orderPrice) desc", Long.class)
                .setMaxResults(1)
                .getSingleResult();

        return entityManager.createQuery("SELECT t FROM  GiftTag t JOIN t.certificates c " +
                "JOIN c.orders o WHERE o.user.id = ?1 GROUP BY t.id ORDER BY COUNT(t.id) DESC ", GiftTag.class)
                .setParameter(1, userId)
                .setMaxResults(1)
                .getSingleResult();
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
        return entityManager.createQuery(FIND_ALL, GiftTag.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
    }
}

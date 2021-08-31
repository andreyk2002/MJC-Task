package com.epam.esm.repository.impl;

import com.epam.esm.entity.GiftTag;
import com.epam.esm.repository.TagRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class TagJpaRepository implements TagRepository {

    public static final String FIND_ALL = "SELECT t FROM GiftTag t";
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public long addTag(GiftTag tag) {
        entityManager.persist(tag);
        entityManager.flush();
        return tag.getId();
    }

    @Override
    @Transactional
    public void updateTag(GiftTag giftTag) {
        entityManager.refresh(giftTag);
    }

    @Override
    public void deleteById(long tagId) {
        GiftTag giftTag = entityManager.find(GiftTag.class, tagId);
        entityManager.remove(giftTag);
    }

    @Override
    public Optional<GiftTag> getById(long id) {
        GiftTag giftTag = entityManager.find(GiftTag.class, id);
        return Optional.ofNullable(giftTag);
    }

    @Override
    public List<GiftTag> getAll() {
        return entityManager.createQuery(FIND_ALL).getResultList();
    }
}

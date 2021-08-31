package com.epam.esm.repository;

import com.epam.esm.entity.GiftTag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {
    long addTag(GiftTag tag);

    void updateTag(GiftTag giftTag);

    void deleteById(long tagId);

    Optional<GiftTag> getById(long id);

    List<GiftTag> getAll();
}

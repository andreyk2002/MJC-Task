package com.epam.esm.repository;

import com.epam.esm.entity.GiftTag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TagRepositoryTest {

    private TagRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        repository = new TagRepository(entityManager);
    }

    @Test
    void testAddTagShouldAdd() {
        GiftTag tagToAdd = new GiftTag(0, "tag");
        GiftTag addedTag = repository.addTag(tagToAdd);
        Assertions.assertEquals(addedTag, tagToAdd);
    }

    @Test
    @Sql({"/deleteAllTags.sql", "addTagWithId42.sql"})
    void testUpdateTagShouldUpdateTag() {
        long id = 42;
        GiftTag tag = new GiftTag(id, "tagName");
        tag.setName("updated name");
        repository.updateTag(tag);
        Optional<GiftTag> updated = repository.getById(id);
        Assertions.assertEquals(updated, Optional.of(tag));
    }

    @Test
    @Sql({"/deleteAllTags.sql", "addTagWithId42.sql"})
    void testDeleteByIdShouldDeleteTag() {
        long id = 42;
        repository.deleteById(id);
        Optional<GiftTag> deleted = repository.getById(id);
        Assertions.assertTrue(deleted.isEmpty());
    }

    @Test
    @Sql({"/deleteAllTags.sql", "addTagWithId421.sql"})
    void testGetByIdShouldReturnTagIfPresent() {
        GiftTag expected = new GiftTag(421, "new tag", Collections.emptySet());
        long addingId = 421;
        Optional<GiftTag> tagById = repository.getById(addingId);
        Assertions.assertEquals(Optional.of(expected), tagById);
    }

    @Test
    @Sql({"/deleteAllTags.sql"})
    void testGetByIdShouldReturnEmptyIfNotFound() {
        long testId = 56565656;
        Optional<GiftTag> empty = repository.getById(testId);
        Assertions.assertTrue(empty.isEmpty());
    }

}
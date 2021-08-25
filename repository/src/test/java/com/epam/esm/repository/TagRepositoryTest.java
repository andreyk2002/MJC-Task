package com.epam.esm.repository;

import com.epam.esm.config.DbConfig;
import com.epam.esm.entity.GiftTag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DbConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TagRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<GiftTag> certificateRowMapper = new TagRowMapper();
    private TagRepository repository;

    @BeforeEach
    void setUp() {
        repository = new TagRepository(jdbcTemplate, certificateRowMapper);
    }


    @Test
    void testAddTagShouldAdd() {
        GiftTag tagToAdd = new GiftTag(0, "tag");
        long addingId = repository.addTag(tagToAdd);
        tagToAdd.setId(addingId);
        Optional<GiftTag> added = repository.getById(addingId);
        Assertions.assertEquals(added, Optional.of(tagToAdd));
    }

    @Test
    void updateTagShouldUpdateTag() {
        GiftTag tag = new GiftTag(0, "tag");
        long addingId = repository.addTag(tag);
        tag.setName("updated name");
        tag.setId(addingId);
        repository.updateTag(tag);
        Optional<GiftTag> updated = repository.getById(addingId);
        Assertions.assertEquals(updated, Optional.of(tag));
    }

    @Test
    void deleteByIdShouldDeleteTag() {
        GiftTag tag = new GiftTag(0, "tag");
        long addingId = repository.addTag(tag);
        repository.deleteById(addingId);
        Optional<GiftTag> deleted = repository.getById(addingId);
        Assertions.assertTrue(deleted.isEmpty());
    }

    @Test
    void getByIdShouldReturnTagIfPresent() {
        GiftTag tag = new GiftTag(0, "tag");
        long addingId = repository.addTag(tag);
        Optional<GiftTag> tagById = repository.getById(addingId);
        tag.setId(addingId);
        Assertions.assertEquals(Optional.of(tag), tagById);
    }

    @Test
    void getByIdShouldReturnEmptyIfNotFound() {
        long testId = 56565656;
        repository.deleteById(testId);
        Optional<GiftTag> empty = repository.getById(testId);
        Assertions.assertTrue(empty.isEmpty());
    }

}
package com.epam.esm.repository;

import com.epam.esm.entity.GiftTag;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

@ExtendWith({SpringExtension.class})
@DataJpaTest(excludeAutoConfiguration = LiquibaseAutoConfiguration.class)
@DBRider
class TagRepositoryTest {

    @Autowired
    private TagRepository repository;

    @Test
    @DataSet(value = "/datasets/allTags.yml", disableConstraints = true)
    @ExpectedDataSet(value = "/datasets/addedTag.yml")
    void testAddTagShouldAdd() {
        GiftTag tagToAdd = new GiftTag(0, "tag");
        repository.save(tagToAdd);
    }

    @Test
    @DataSet(value = "/datasets/tag.yml", disableConstraints = true)
    @ExpectedDataSet(value = "/datasets/updatedTag.yml")
    void testUpdateTagShouldUpdateTag() {
        long id = 42;
        GiftTag tag = new GiftTag(id, "updated name");
        repository.save(tag);
    }

    @Test
    @DataSet(value = "/datasets/tag.yml", disableConstraints = true)
    @ExpectedDataSet(value = "/datasets/emptyTags.yml")
    void testDeleteByIdShouldDeleteTag() {
        long id = 42;
        repository.deleteById(id);
    }

    @Test
    @DataSet(value = "/datasets/allTags.yml", disableConstraints = true)
    void testGetByIdShouldReturnTagIfPresent() {
        long id = 3;
        GiftTag expected = new GiftTag(id, "third", Collections.emptySet());
        Optional<GiftTag> tagById = repository.findById(id);
        Assertions.assertEquals(Optional.of(expected), tagById);
    }

    @Test
    @DataSet(value = "/datasets/allTags.yml", disableConstraints = true)
    void testGetByIdShouldReturnEmptyIfNotFound() {
        long testId = 56565656;
        Optional<GiftTag> empty = repository.findById(testId);
        Assertions.assertTrue(empty.isEmpty());
    }

}
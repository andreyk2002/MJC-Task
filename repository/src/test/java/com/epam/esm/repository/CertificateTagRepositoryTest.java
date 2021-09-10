package com.epam.esm.repository;

import com.epam.esm.entity.GiftTag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJdbcTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CertificateTagRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<GiftTag> tagRowMapper = new TagRowMapper();

    private CertificateTagJdbcRepository repository;


    @BeforeEach
    void setUp() {
        repository = new CertificateTagJdbcRepository(jdbcTemplate, tagRowMapper);
    }


    @Test
    @Sql({"/insertCertificateTags.sql"})
    void testGetTagByCertificateIdShouldReturnAllTagsWithSpecifiedId() {
        GiftTag firstTag = new GiftTag(19, "first tag");
        GiftTag secondTag = new GiftTag(22, "second tag");
        long insertedCertificateId = 77;
        List<GiftTag> certificateTags = repository.getTagsByCertificateId(insertedCertificateId);
        Assertions.assertEquals(Arrays.asList(firstTag, secondTag), certificateTags);
    }

    @Test
    @Sql({"/insertCertificateTags.sql"})
    void testDeleteByTagId() {
        GiftTag firstTag = new GiftTag(19, "first tag");
        GiftTag secondTag = new GiftTag(22, "second tag");
        long insertedCertificateId = 77;
        repository.deleteByTagId(firstTag.getId());
        List<GiftTag> certificateTags = repository.getTagsByCertificateId(insertedCertificateId);
        Assertions.assertEquals(Collections.singletonList(secondTag), certificateTags);
    }

    @Test
    void testDeleteByCertificateId() {
        long insertedCertificateId = 77;
        repository.deleteByCertificateId(insertedCertificateId);
        List<GiftTag> empty = repository.getTagsByCertificateId(insertedCertificateId);
        Assertions.assertTrue(empty.isEmpty());
    }

}
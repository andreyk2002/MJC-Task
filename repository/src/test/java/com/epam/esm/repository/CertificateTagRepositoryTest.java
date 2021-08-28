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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DbConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CertificateTagRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<GiftTag> tagRowMapper = new TagRowMapper();

    private CertificateTagRepository repository;
    public static final GiftTag FIRST_TAG = new GiftTag(19, "first tag");
    public static final GiftTag SECOND_TAG = new GiftTag(22, "second tag");
    public static final long INSERTED_CERTIFICATE_ID = 77;

    @BeforeEach
    void setUp() {
        repository = new CertificateTagRepository(jdbcTemplate, tagRowMapper);
    }


    @Test
    @Sql({"/insertCertificateTags.sql"})
    void testGetTagByCertificateIdShouldReturnAllTagsWithSpecifiedId() {
        List<GiftTag> certificateTags = repository.getTagsByCertificateId(INSERTED_CERTIFICATE_ID);
        Assertions.assertEquals(Arrays.asList(FIRST_TAG, SECOND_TAG), certificateTags);
    }

    @Test
    @Sql({"/insertCertificateTags.sql"})
    void testDeleteByTagId() {
        repository.deleteByTagId(FIRST_TAG.getId());
        List<GiftTag> certificateTags = repository.getTagsByCertificateId(INSERTED_CERTIFICATE_ID);
        Assertions.assertEquals(Collections.singletonList(SECOND_TAG), certificateTags);
    }

    @Test
    void testDeleteByCertificateId() {
        repository.deleteByCertificateId(INSERTED_CERTIFICATE_ID);
        List<GiftTag> empty = repository.getTagsByCertificateId(INSERTED_CERTIFICATE_ID);
        Assertions.assertTrue(empty.isEmpty());
    }

}
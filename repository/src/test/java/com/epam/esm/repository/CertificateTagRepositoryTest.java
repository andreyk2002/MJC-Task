package com.epam.esm.repository;

import com.epam.esm.config.DbConfig;
import com.epam.esm.entity.GiftTag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DbConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CertificateTagRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<GiftTag> certificateRowMapper = new TagRowMapper();
    private CertificateTagRepository repository;

    @BeforeEach
    void setUp() {
        repository = new CertificateTagRepository(jdbcTemplate, certificateRowMapper);
    }


    @Test
    void getTagByCertificateId() {

    }

    @Test
    void deleteByTagId() {
    }

    @Test
    void deleteByCertificateId() {
    }

    @Test
    void addCertificateTag() {
    }
}
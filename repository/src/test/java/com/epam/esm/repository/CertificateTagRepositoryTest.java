package com.epam.esm.repository;

import com.epam.esm.config.DbConfig;
import com.epam.esm.entity.GiftCertificate;
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

import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DbConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CertificateTagRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<GiftTag> tagRowMapper = new TagRowMapper();
    private final RequestBuilder builder = new RequestBuilder();
    private final RowMapper<GiftCertificate> certificateRowMapper = new CertificateRowMapper();

    private final CertificateBuilder certificateBuilder = new CertificateBuilder();
    private CertificateTagRepository repository;
    private TagRepository tagRepository;
    private CertificateRepository certificateRepository;

    @BeforeEach
    void setUp() {
        repository = new CertificateTagRepository(jdbcTemplate, tagRowMapper);
        tagRepository = new TagRepository(jdbcTemplate, tagRowMapper);
        certificateRepository = new CertificateRepository(jdbcTemplate, certificateRowMapper, builder);
    }


    @Test
    void testGetTagByCertificateIdShouldReturnAllTagsWithSpecifiedId() {
        GiftCertificate giftCertificate = certificateBuilder.buildCertificate();
        GiftTag firstTag = new GiftTag(1, "firstName");
        GiftTag secondTag = new GiftTag(2, "secondName");
        long firstId = tagRepository.addTag(firstTag);
        long secondId = tagRepository.addTag(secondTag);
        long certificateId = certificateRepository.addCertificate(giftCertificate);
        repository.addCertificateTag(firstId, certificateId);
        repository.addCertificateTag(secondId, certificateId);
        List<GiftTag> certificateTags = repository.getTagsByCertificateId(certificateId);
        Assertions.assertEquals(certificateTags.size(), 2);

        Assertions.assertEquals(firstId, certificateTags.get(0).getId());
        Assertions.assertEquals(secondId, certificateTags.get(1).getId());
        Assertions.assertEquals(firstTag.getName(), certificateTags.get(0).getName());
        Assertions.assertEquals(secondTag.getName(), certificateTags.get(1).getName());
    }

    @Test
    void deleteByTagId() {
        GiftCertificate giftCertificate = certificateBuilder.buildCertificate();
        GiftTag firstTag = new GiftTag(1, "sdfas");
        GiftTag secondTag = new GiftTag(2, "dfasdfa");
        long certificateId = certificateRepository.addCertificate(giftCertificate);
        long firstId = tagRepository.addTag(firstTag);
        long secondId = tagRepository.addTag(secondTag);
        repository.addCertificateTag(firstId, certificateId);
        repository.addCertificateTag(secondId, certificateId);
        repository.deleteByTagId(firstId);
        List<GiftTag> certificateTags = repository.getTagsByCertificateId(certificateId);
        Assertions.assertEquals(certificateTags.size(), 1);
        Assertions.assertEquals(certificateTags.get(0).getName(), secondTag.getName());
        Assertions.assertEquals(certificateTags.get(0).getId(), secondId);
    }

    @Test
    void deleteByCertificateId() {
        GiftCertificate giftCertificate = certificateBuilder.buildCertificate();
        GiftTag firstTag = new GiftTag(1, "sojdef");
        GiftTag secondTag = new GiftTag(2, "ddd");
        long firstId = tagRepository.addTag(firstTag);
        long secondId = tagRepository.addTag(secondTag);
        long certificateId = certificateRepository.addCertificate(giftCertificate);
        repository.addCertificateTag(firstId, certificateId);
        repository.addCertificateTag(secondId, certificateId);
        repository.deleteByCertificateId(certificateId);
        List<GiftTag> empty = repository.getTagsByCertificateId(certificateId);
        Assertions.assertTrue(empty.isEmpty());
    }

}
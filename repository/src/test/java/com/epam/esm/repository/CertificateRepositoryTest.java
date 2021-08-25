package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


class CertificateRepositoryTest {

    private CertificateRepository repository;

    //TODO:Remove hikari
    @BeforeEach
    void setUp() {
        HikariConfig config = new HikariConfig("src/test/resources/testDb.properties");
        DataSource dataSource = new HikariDataSource(config);
        JdbcTemplate template = new JdbcTemplate(dataSource);
        RequestBuilder builder = new RequestBuilder();
        RowMapper<GiftCertificate> giftCertificateRowMapper = new CertificateRowMapper();
        repository = new CertificateRepository(template, giftCertificateRowMapper, builder);
    }

    @Test
    public void testGetByIdShouldReturnCertificateWhenItExisting() {
        long EXISTING_ID = 2;
        Optional<GiftCertificate> optionalCertificate = repository.getById(EXISTING_ID);
        GiftCertificate giftCertificate = buildCertificate(EXISTING_ID);
        Assertions.assertEquals(Optional.of(giftCertificate), optionalCertificate);
    }

    @Test
    public void testGetByIdShouldReturnEmptyWhenCertificateNotExisting() {
        long NOT_EXISTING_ID = 666;
        Optional<GiftCertificate> certificateOptional = repository.getById(NOT_EXISTING_ID);
        Assertions.assertFalse(certificateOptional.isPresent());
    }

    @Test
    public void testGetAllShouldReturnAllCertificates() {
        List<GiftCertificate> all = repository.getAll();
        GiftCertificate giftCertificate = buildCertificate();
        List<GiftCertificate> expectedResult = Collections.singletonList(giftCertificate);
        Assertions.assertEquals(expectedResult, all);
    }

    @Test
    public void testAddCertificateShouldAddCertificate() {
        GiftCertificate giftCertificate = buildCertificate();
        long insertedId = repository.addCertificate(giftCertificate);
        Optional<GiftCertificate> insertedCertificate = repository.getById(insertedId);
        giftCertificate.setId(insertedId);
        Assertions.assertTrue(insertedCertificate.isPresent());
        //We should ignore createDate and updateDate because they decided in repo.addCertificate()
        GiftCertificate inserted = insertedCertificate.get();
        giftCertificate.setLastUpdateDate(inserted.getLastUpdateDate());
        giftCertificate.setCreateDate(inserted.getCreateDate());
        Assertions.assertEquals(inserted, giftCertificate);

    }

    @Test
    public void testDeleteShouldDeleteCertificate() {
        GiftCertificate giftCertificate = buildCertificate();
        long insertedId = repository.addCertificate(giftCertificate);
        repository.deleteById(insertedId);
        Optional<GiftCertificate> deleted = repository.getById(insertedId);
        Assertions.assertTrue(deleted.isEmpty());
    }

    @Test
    public void testUpdateShouldUpdateCertificate() {
        GiftCertificate giftCertificate = buildCertificate();
        long insertedId = repository.addCertificate(giftCertificate);
        giftCertificate.setDescription("changed");
        giftCertificate.setName("changed");
        giftCertificate.setId(insertedId);
        repository.updateCertificate(giftCertificate);
        Optional<GiftCertificate> updated = repository.getById(insertedId);
        Assertions.assertTrue(updated.isPresent());
        GiftCertificate updatedCertificate = updated.get();
        giftCertificate.setLastUpdateDate(updatedCertificate.getLastUpdateDate());
        Assertions.assertEquals(updatedCertificate, giftCertificate);
    }

    public GiftCertificate buildCertificate(long id) {
        return GiftCertificate.builder()
                .id(id)
                .price(new BigDecimal("19"))
                .duration(0)
                .createDate(LocalDateTime.of(2002, 8, 12, 3, 11))
                .lastUpdateDate(LocalDateTime.of(2005, 8, 12, 3, 11))
                .build();
    }

    public GiftCertificate buildCertificate() {
        return buildCertificate(0);
    }

}
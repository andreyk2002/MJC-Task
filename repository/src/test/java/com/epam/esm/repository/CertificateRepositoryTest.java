package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class CertificateRepositoryTest {

    private CertificateRepository repository;
    private final long EXISTING_ID = 2;
    private final long NOT_EXISTING_ID = 666;
    private final GiftCertificate giftCertificate = GiftCertificate.builder()
            .id(2)
            .price(new BigDecimal("19"))
            .createDate(LocalDateTime.of(2002, 8, 12, 3, 11))
            .lastUpdateDate(LocalDateTime.of(2005, 8, 12, 3, 11))
            .build();

    @BeforeEach
    void setUp() {
//        HikariConfig config = new HikariConfig("testDb.properties");
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.h2.Driver");
        config.setJdbcUrl("jdbc:h2:~/test");
        config.setPassword("");
        config.setUsername("sa");
        DataSource dataSource = new HikariDataSource(config);
        JdbcTemplate template = new JdbcTemplate(dataSource);
        repository = new CertificateRepository(template);
    }

    @Test
    public void testGetByIdShouldReturnCertificateWhenItExisting() {
        Optional<GiftCertificate> optionalCertificate = repository.getById(EXISTING_ID);
        Assertions.assertEquals(Optional.of(giftCertificate), optionalCertificate);
    }

    @Test
    public void testGetByIdShouldReturnEmptyWhenCertificateNotExisting() {
        Optional<GiftCertificate> certificateOptional = repository.getById(NOT_EXISTING_ID);
        Assertions.assertFalse(certificateOptional.isPresent());
    }

    @Test
    public void testGetAllShouldReturnAllCertificates() {
        List<GiftCertificate> all = repository.getAll();
        List<GiftCertificate> expectedResult = Collections.singletonList(giftCertificate);
        Assertions.assertEquals(expectedResult, all);
    }

}
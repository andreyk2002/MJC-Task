package com.epam.esm.repository;

import com.epam.esm.config.DbConfig;
import com.epam.esm.entity.GiftCertificate;
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

import java.util.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DbConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CertificateRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final CertificateBuilder certificateBuilder = new CertificateBuilder();
    private final RequestBuilder builder = new RequestBuilder();
    private final RowMapper<GiftCertificate> certificateRowMapper = new CertificateRowMapper();
    private CertificateRepository repository;

    @BeforeEach
    void setUp() {
        repository = new CertificateRepository(jdbcTemplate, certificateRowMapper, builder);
    }

    @Test
    public void testGetByIdShouldReturnCertificateWhenItExisting() {
        GiftCertificate certificate = certificateBuilder.buildCertificate();
        long insertedId = repository.addCertificate(certificate);
        Optional<GiftCertificate> optionalCertificate = repository.getById(insertedId);
        Assertions.assertTrue(optionalCertificate.isPresent());
        Assertions.assertTrue(equalsIgnoreIdAndDate(optionalCertificate.get(), certificate));
    }

    @Test
    public void testGetByIdShouldReturnEmptyWhenCertificateNotExisting() {
        long notExistingId = 666;
        Optional<GiftCertificate> certificateOptional = repository.getById(notExistingId);
        Assertions.assertFalse(certificateOptional.isPresent());
    }

    @Test
    public void testGetAllShouldReturnAllCertificates() {
        repository.deleteAll();
        GiftCertificate first = certificateBuilder.buildCertificate();
        GiftCertificate second = certificateBuilder.buildCertificate();
        repository.addCertificate(first);
        repository.addCertificate(second);
        List<GiftCertificate> all = repository.getAll();
        List<GiftCertificate> expectedResult = Arrays.asList(first, second);
        Assertions.assertEquals(expectedResult.size(), all.size());
        Assertions.assertTrue(equalsIgnoreIdAndDate(expectedResult.get(0), all.get(0)));
        Assertions.assertTrue(equalsIgnoreIdAndDate(expectedResult.get(1), all.get(1)));
    }

    @Test
    public void testAddCertificateShouldAddCertificate() {
        GiftCertificate giftCertificate = certificateBuilder.buildCertificate();
        long insertedId = repository.addCertificate(giftCertificate);
        Optional<GiftCertificate> insertedCertificate = repository.getById(insertedId);
        giftCertificate.setId(insertedId);
        Assertions.assertTrue(insertedCertificate.isPresent());
        GiftCertificate inserted = insertedCertificate.get();
        Assertions.assertTrue(equalsIgnoreIdAndDate(inserted, giftCertificate));

    }

    @Test
    public void testDeleteShouldDeleteCertificate() {
        GiftCertificate giftCertificate = certificateBuilder.buildCertificate();
        long insertedId = repository.addCertificate(giftCertificate);
        repository.deleteById(insertedId);
        Optional<GiftCertificate> deleted = repository.getById(insertedId);
        Assertions.assertTrue(deleted.isEmpty());
    }

    @Test
    public void testUpdateShouldUpdateCertificate() {
        GiftCertificate giftCertificate = certificateBuilder.buildCertificate();
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

    @Test
    public void testDeleteAllShouldDeleteAllCertificates() {
        repository.deleteAll();
        List<GiftCertificate> allCertificates = repository.getAll();
        Assertions.assertEquals(Collections.emptyList(), allCertificates);
    }

    //We should ignore createDate and updateDate because they decided in repo.addCertificate()
    private boolean equalsIgnoreIdAndDate(GiftCertificate first, GiftCertificate second) {
        return Objects.equals(first.getName(), second.getName())
                && Objects.equals(first.getDescription(), second.getDescription())
                && Objects.equals(first.getPrice(), second.getPrice())
                && Objects.equals(first.getDuration(), second.getDuration());

    }


}
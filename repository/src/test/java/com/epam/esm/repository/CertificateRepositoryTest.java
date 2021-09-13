package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@DBRider
class CertificateRepositoryTest {

    private CertificateRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        repository = new CertificateRepository(entityManager);
    }


    @Test
    @DataSet(value = "/datasets/certificates.yml", cleanBefore = true)
    @ExpectedDataSet("/datasets/addedCertificate.yml")
    public void testAddCertificateShouldAddCertificate() {
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .name("added")
                .build();
        repository.addCertificate(giftCertificate);
    }

    @Test
    @DataSet(value = "/datasets/certificate.yml", disableConstraints = true, cleanBefore = true)
    public void testGetByIdShouldReturnCertificateWhenItExisting() {
        long existingId = 420;
        GiftCertificate certificate = GiftCertificate.builder()
                .id(existingId)
                .name("certificate")
                .price(new BigDecimal("10.00"))
                .tags(Collections.emptySet())
                .orders(Collections.emptySet())
                .build();
        Optional<GiftCertificate> optionalCertificate = repository.getById(existingId);
        Assertions.assertEquals(Optional.of(certificate), optionalCertificate);
    }

    @Test
    @DataSet(value = "/datasets/certificate.yml", disableConstraints = true, cleanBefore = true)
    public void testGetByIdShouldReturnEmptyWhenCertificateNotExisting() {
        long notExistingId = 666;
        Optional<GiftCertificate> certificateOptional = repository.getById(notExistingId);
        Assertions.assertFalse(certificateOptional.isPresent());
    }

    @Test
    @DataSet(value = "/datasets/certificates.yml", disableConstraints = true, cleanBefore = true)
    public void testGetAllShouldReturnAllCertificates() {
        GiftCertificate first = GiftCertificate.builder()
                .id(1)
                .name("first")
                .tags(Collections.emptySet())
                .orders(Collections.emptySet())
                .build();
        GiftCertificate second = GiftCertificate.builder()
                .id(2)
                .name("second")
                .tags(Collections.emptySet())
                .orders(Collections.emptySet())
                .build();
        List<GiftCertificate> expectedResult = Arrays.asList(first, second);

        List<GiftCertificate> all = repository.getPage(0, 100);
        Assertions.assertEquals(expectedResult, all);
    }

    @Test
    @DataSet(value = "/datasets/certificateForDeleting.yml", cleanBefore = true)
    @ExpectedDataSet("/datasets/empty.yml")
    public void testDeleteShouldDeleteCertificate() {
        long existingId = 705;
        repository.deleteById(existingId);
    }

    @Test
    @DataSet(value = "/datasets/certificate.yml", cleanBefore = true)
    @ExpectedDataSet("/datasets/updatedCertificate.yml")
    public void testUpdateShouldUpdateCertificate() {
        long insertedId = 420;
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .id(insertedId)
                .price(new BigDecimal("23"))
                .name("changed name")
                .description("changed")
                .build();
        repository.updateCertificate(giftCertificate);
    }
}


package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
//@DBUnit
class CertificateRepositoryTest {

    private final CertificateBuilder certificateBuilder = new CertificateBuilder();

    private CertificateRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        repository = new CertificateRepository(entityManager);
    }

    @Test
    @Sql({"/deleteAllCertificates.sql", "/insertCertificateWithId420.sql"})
    public void testGetByIdShouldReturnCertificateWhenItExisting() {
        GiftCertificate certificate = certificateBuilder.buildCertificate("certificate name");
        long existingId = 420;
        Optional<GiftCertificate> optionalCertificate = repository.getById(existingId);
        Assertions.assertTrue(optionalCertificate.isPresent());
        Assertions.assertTrue(equalsIgnoreIdAndDate(optionalCertificate.get(), certificate));
    }

    @Test
    @Sql({"/deleteAllCertificates.sql"})
    public void testGetByIdShouldReturnEmptyWhenCertificateNotExisting() {
        long notExistingId = 666;
        Optional<GiftCertificate> certificateOptional = repository.getById(notExistingId);
        Assertions.assertFalse(certificateOptional.isPresent());
    }

    @Test
    @Sql({"/deleteAllCertificates.sql", "/insertCertificates.sql"})
    public void testGetAllShouldReturnAllCertificates() {
        GiftCertificate first = certificateBuilder.buildCertificate("first");
        GiftCertificate second = certificateBuilder.buildCertificate("second");
        List<GiftCertificate> expectedResult = Arrays.asList(first, second);

        List<GiftCertificate> all = repository.getPage(0, 100);
        Assertions.assertEquals(expectedResult.size(), all.size());
        Assertions.assertTrue(equalsIgnoreIdAndDate(expectedResult.get(0), all.get(0)));
        Assertions.assertTrue(equalsIgnoreIdAndDate(expectedResult.get(1), all.get(1)));
    }

    @Test
    public void testAddCertificateShouldAddCertificate() {
        GiftCertificate giftCertificate = certificateBuilder.buildCertificate("certificate123");
        GiftCertificate certificate = repository.addCertificate(giftCertificate);
        Assertions.assertEquals(giftCertificate, certificate);
    }

    @Test
    @Sql({"/deleteAllCertificates.sql", "/insertCertificateWithId420.sql"})
    public void testDeleteShouldDeleteCertificate() {
        long existingId = 420;
        repository.deleteById(existingId);
        Optional<GiftCertificate> deleted = repository.getById(existingId);
        Assertions.assertTrue(deleted.isEmpty());
    }

    @Test
    @Sql({"/deleteAllCertificates.sql", "/insertCertificateWithId420.sql"})
    public void testUpdateShouldUpdateCertificate() {
        GiftCertificate giftCertificate = certificateBuilder.buildCertificate("certificate name");
        long insertedId = 420;
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


    //We should ignore createDate and updateDate because they decided in repo.addCertificate()
    private boolean equalsIgnoreIdAndDate(GiftCertificate first, GiftCertificate second) {
        return Objects.equals(first.getName(), second.getName())
                && Objects.equals(first.getDescription(), second.getDescription())
                && Objects.equals(first.getPrice(), second.getPrice())
                && Objects.equals(first.getDuration(), second.getDuration());

    }


}


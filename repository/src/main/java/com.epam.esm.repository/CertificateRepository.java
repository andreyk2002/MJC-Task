package com.epam.esm.repository;


import com.epam.esm.entity.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CertificateRepository {
    private static final String DELETE_QUERY = "DELETE FROM gift_certificate WHERE id = ?";
    private static final String FIND_BY_ID = "SELECT * FROM gift_certificate WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM gift_certificate";
    private static final String UPDATE = "UPDATE * FROM gift_certificate WHERE id = ? SET name = ?," +
            "description = ?, price = ?, duration = ?, creation_date = ?, last_update_date = ?";
    private static final String FIND_BY_TAG_NAME = "SELECT * FROM  gift_certificate gc " +
            "JOIN certificate_tag ct " +
            "ON ct.certificate_id = gc.id " +
            "JOIN tag t " +
            "ON ct.tag_id = t.id " +
            "WHERE t.name = ?";
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<GiftCertificate> certificateMapper;

    private static final String ADD_QUERY = "INSERT INTO gift_certificate (name, description, price, duration," +
            " create_date, last_update_date) VALUES(?,?,?,?,?,?)";

    @Autowired
    public CertificateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.certificateMapper = new CertificateRowMapper();
    }

    public int addCertificate(GiftCertificate giftCertificate) {
        return jdbcTemplate.update(
                ADD_QUERY, giftCertificate.getName(), giftCertificate.getDescription(),
                giftCertificate.getPrice(), giftCertificate.getDuration(), giftCertificate.getCreateDate(),
                giftCertificate.getLastUpdateDate()
        );
    }

    public int deleteById(long id) {
        return jdbcTemplate.update(DELETE_QUERY, id);
    }

    public void updateCertificate(GiftCertificate giftCertificate) {

        long id = giftCertificate.getId();
        GiftCertificate oldVersion = getById(id);

        LocalDateTime createDate = giftCertificate.getCreateDate();
        String description = giftCertificate.getDescription();
        String name = giftCertificate.getName();
        LocalDateTime lastUpdateDate = giftCertificate.getLastUpdateDate();
        BigDecimal price = giftCertificate.getPrice();
        int duration = giftCertificate.getDuration();
        jdbcTemplate.update(UPDATE, id, name, description, price, duration, createDate, lastUpdateDate);
    }

    public List<GiftCertificate> getAll() {
        return jdbcTemplate.query(FIND_ALL, certificateMapper);
    }

    public GiftCertificate getById(long id) {
        return jdbcTemplate.queryForObject(FIND_BY_ID, certificateMapper, id);
    }

    public GiftCertificate findByTagName(String tagName) {
        return jdbcTemplate.queryForObject(FIND_BY_TAG_NAME, certificateMapper, tagName);
    }
}

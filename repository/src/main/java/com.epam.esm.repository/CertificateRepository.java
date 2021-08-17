package com.epam.esm.repository;


import com.epam.esm.entity.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class CertificateRepository {
    private static final String DELETE_QUERY = "DELETE FROM gift_certificate WHERE id = ?";
    private static final String FIND_BY_ID = "SELECT * FROM gift_certificate WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM gift_certificate";
    private static final String UPDATE = "UPDATE * FROM gift_certificate WHERE id = ? SET name = ?," +
            "description = ?, price = ?, duration = ?, creation_date = ?, last_update_date = ?";
    private static final String FIND_SORTED = "SELECT * FROM gift_certificate ORDER BY ? ?";
    private static final String FIND_BY_KEYWORD = "SELECT * FROM gift_certificate WHERE name LIKE " +
            "concat('%', ?, '%') OR description LIKE concat('%', ?, '%')";
    private static final String FIND_BY_TAG_NAME = "SELECT * FROM gift_certificate gc JOIN certificate_tag ct ON " +
            "gc.id = ct.id JOIN tag t ON t.id = ct.tag_id WHERE t.name = ? ";
    private static final String FIND_BY_TAG_NAME_KEYWORD = "SELECT * FROM gift_certificate gc JOIN certificate_tag ct ON " +
            "gc.id = ct.certificate_id JOIN tag t ON t.id = ct.tag_id WHERE t.name = ? AND (gc.name LIKE " +
            "concat('%', ?, '%') OR gc.description LIKE concat('%', ?, '%')) ";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<GiftCertificate> certificateMapper;

    private static final String ADD_QUERY = "INSERT INTO gift_certificate (name, description, price, duration," +
            " create_date, last_update_date) VALUES(?,?,?,?,?,?)";

    @Autowired
    public CertificateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.certificateMapper = new CertificateRowMapper();
    }

    public long addCertificate(GiftCertificate giftCertificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                (connection) -> {
                    PreparedStatement statement =
                            connection.prepareStatement(ADD_QUERY, new String[]{
                                    "name", "description", "price", "duration", "creation_date", "last_update_date"
                            });
                    statement.setString(1, giftCertificate.getName());
                    statement.setString(2, giftCertificate.getDescription());
                    statement.setBigDecimal(3, giftCertificate.getPrice());
                    statement.setObject(4, giftCertificate.getDuration());
                    statement.setObject(5, LocalDateTime.now());
                    statement.setObject(6, LocalDateTime.now());
                    return statement;
                },
                keyHolder);
        Number key = keyHolder.getKey();
        return Objects.requireNonNull(key).longValue();
    }


    public int deleteById(long id) {
        return jdbcTemplate.update(DELETE_QUERY, id);
    }

    public void updateCertificate(GiftCertificate giftCertificate) {
        long id = giftCertificate.getId();
        LocalDateTime createDate = giftCertificate.getCreateDate();
        String description = giftCertificate.getDescription();
        String name = giftCertificate.getName();
        LocalDateTime lastUpdateDate = LocalDateTime.now();
        BigDecimal price = giftCertificate.getPrice();
        int duration = giftCertificate.getDuration();
        jdbcTemplate.update(UPDATE, id, name, description, price, duration, createDate, lastUpdateDate);
    }

    public List<GiftCertificate> getAll() {
        return jdbcTemplate.query(FIND_ALL, certificateMapper);
    }

    public Optional<GiftCertificate> getById(long id) {
        try {
            GiftCertificate giftCertificate = jdbcTemplate.queryForObject(FIND_BY_ID, certificateMapper, id);
            return Optional.of(giftCertificate);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<GiftCertificate> getAllSorted(String sortOrder, String field) {
        String finalQuery = String.format("%s ORDER BY %s %s", FIND_SORTED, field, sortOrder);
        return jdbcTemplate.query(finalQuery, certificateMapper);
    }

    public List<GiftCertificate> getByKeyword(String keyword, String sortOrder, String field) {
        String finalQuery = String.format("%s ORDER BY %s %s", FIND_BY_KEYWORD, field, sortOrder);
        return jdbcTemplate.query(finalQuery, certificateMapper, keyword, keyword);
    }

    public List<GiftCertificate> getByTagName(String tagName, String sortOrder, String field) {
        String finalQuery = String.format("%s ORDER BY %s %s", FIND_BY_TAG_NAME, field, sortOrder);
        return jdbcTemplate.query(finalQuery, certificateMapper, tagName);
    }

    public List<GiftCertificate> getByTagNameAndKeyword(String keyword, String tagName, String sortOrder, String field) {
        String finalQuery = String.format("%s ORDER BY %s %s", FIND_BY_TAG_NAME_KEYWORD, field, sortOrder);
        return jdbcTemplate.query(finalQuery, certificateMapper, tagName, keyword, keyword);
    }
}

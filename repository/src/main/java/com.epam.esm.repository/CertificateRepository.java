package com.epam.esm.repository;


import com.epam.esm.entity.GiftCertificate;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class CertificateRepository {
    private static final String DELETE_QUERY = "DELETE FROM gift_certificate WHERE id = ?";
    private static final String FIND_BY_ID = "SELECT * FROM gift_certificate WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM gift_certificate";
    private static final String UPDATE = "UPDATE gift_certificate SET name = ?," +
            "description = ?, price = ?, duration = ?, create_date = ?, last_update_date = ? WHERE id = ?";

    private static final Logger LOGGER = LogManager.getLogger(CertificateRepository.class);
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<GiftCertificate> certificateMapper;
    private final RequestBuilder requestBuilder;

    private static final String ADD_QUERY = "INSERT INTO gift_certificate (name, description, price, duration," +
            " create_date, last_update_date) VALUES(?,?,?,?,?,?)";


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
        return key.longValue();
    }


    public int deleteById(long id) {
        return jdbcTemplate.update(DELETE_QUERY, id);
    }

    public void updateCertificate(GiftCertificate giftCertificate) {
        jdbcTemplate.update(
                UPDATE,
                giftCertificate.getName(),
                giftCertificate.getDescription(),
                giftCertificate.getPrice(),
                giftCertificate.getDuration(),
                giftCertificate.getCreateDate(),
                LocalDateTime.now(),
                giftCertificate.getId()
        );
    }

    public List<GiftCertificate> getAll() {
        return jdbcTemplate.query(FIND_ALL, certificateMapper);
    }

    public Optional<GiftCertificate> getById(long id) {
        try {
            GiftCertificate giftCertificate = jdbcTemplate.queryForObject(FIND_BY_ID, certificateMapper, id);
            return Optional.ofNullable(giftCertificate);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
            return Optional.empty();
        }
    }

    public List<GiftCertificate> getAllSorted(String keyword, String tagName, String sortOrder, String field) {
        String query = requestBuilder.buildSortRequest(keyword, tagName, sortOrder, field);
        return jdbcTemplate.query(query, certificateMapper);
    }

}

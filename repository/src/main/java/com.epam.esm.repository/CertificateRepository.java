package com.epam.esm.repository;


import com.epam.esm.entity.GiftCertificate;
import lombok.AllArgsConstructor;
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

/**
 * CertificateRepository provides functionality for interaction with storage,
 * which contains data about {@link GiftCertificate} entities
 */


@Repository
@AllArgsConstructor
public class CertificateRepository {
    private static final String DELETE_QUERY = "DELETE FROM gift_certificate WHERE id = ?";
    private static final String FIND_BY_ID = "SELECT * FROM gift_certificate WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM gift_certificate";
    private static final String UPDATE = "UPDATE gift_certificate SET name = ?," +
            "description = ?, price = ?, duration = ?, create_date = ?, last_update_date = ? WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<GiftCertificate> certificateMapper;
    private final RequestBuilder requestBuilder;

    private static final String ADD_QUERY = "INSERT INTO gift_certificate (name, description, price, duration," +
            " create_date, last_update_date) VALUES(?,?,?,?,?,?)";


    /**
     * Adds an instance of {@link GiftCertificate} into the storage
     *
     * @param giftCertificate instance of certificate, needed to be added
     * @return ID of inserted certificate
     */
    public long addCertificate(GiftCertificate giftCertificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                (connection) -> {
                    PreparedStatement statement =
                            connection.prepareStatement(ADD_QUERY, new String[]{
                                    "name", "description", "price", "duration", "create_date", "last_update_date"
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


    /**
     * Removes a certificate with specified id from storage if present
     *
     * @param id - ID of certificate to be removed
     */
    public void deleteById(long id) {
        jdbcTemplate.update(DELETE_QUERY, id);
    }

    /**
     * Updates an instance of {@link GiftCertificate} in the storage
     *
     * @param giftCertificate instance of certificate, needed to be updated
     */
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

    /**
     * Get list of all certificates, which are present in the storage
     *
     * @return List of all present certificates
     */
    public List<GiftCertificate> getAll() {
        return jdbcTemplate.query(FIND_ALL, certificateMapper);
    }

    /**
     * Searches for certificate with specified id in the storage
     *
     * @param id - ID of certificate
     * @return instance of GiftCertificate  wrapped with {@link Optional} if present,
     * else returns {@link Optional#empty()}
     */
    public Optional<GiftCertificate> getById(long id) {
        try {
            GiftCertificate giftCertificate = jdbcTemplate.queryForObject(FIND_BY_ID, certificateMapper, id);
            return Optional.ofNullable(giftCertificate);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * Searches list of all certificates depends on keyword (part of name or description) or/and tag name
     * in specified order
     *
     * @param keyword   part of name or description which certificates should contain. In case if keyword is null
     *                  all certificates are specified to this criteria
     * @param tagName   name of the tag which certificate should contain. In case if keyword is null
     *                  all certificates are specified to this criteria
     * @param sortOrder type of sort order (ascending, descending)
     * @param field     name of the field by which certificates should be ordered
     * @return List of certificates which applied to the mentioned criterias
     */
    public List<GiftCertificate> getAllSorted(String keyword, String tagName, String sortOrder, String field) {
        RequestResult requestResult = requestBuilder.buildSortRequest(keyword, tagName, sortOrder, field);
        String query = requestResult.getQuery();
        String[] params = requestResult.getParams();
        return jdbcTemplate.query(query, certificateMapper, (Object[]) params);
    }

}

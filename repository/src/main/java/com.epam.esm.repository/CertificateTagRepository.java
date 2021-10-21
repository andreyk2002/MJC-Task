package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftTag;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * CertificateTagRepository provides functionality for interaction with storage,
 * which contains information about connections between {@link GiftCertificate} and {@link GiftTag} entities
 */


@Repository
@AllArgsConstructor
public class CertificateTagRepository {

    private static final String FIND_BY_CERTIFICATE = "SELECT t.id AS id, t.name AS name FROM certificate_tag ct " +
            "JOIN tag t ON ct.tag_id = t.id WHERE ct.certificate_id = ?";
    private static final String DELETE_BY_CERTIFICATE_ID = "DELETE FROM certificate_tag WHERE certificate_id = ?";
    private static final String DELETE_BY_TAG_ID = "DELETE FROM certificate_tag WHERE tag_id = ?";
    private static final String ADD_CERTIFICATE_TAG = "INSERT INTO certificate_tag (tag_id, certificate_id) VALUES (? , ?)";
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<GiftTag> tagRowMapper;

    /**
     * Gets all tags of specified certificate
     *
     * @param id - id of specified certificate
     * @return List of all tags for specified certificate
     */
    public List<GiftTag> getTagsByCertificateId(long id) {
        return jdbcTemplate.query(FIND_BY_CERTIFICATE, tagRowMapper, id);
    }

    /**
     * Removes all records which connected with specified tag
     *
     * @param tagId - id of the tag by which records will be removed
     */
    public void deleteByTagId(long tagId) {
        jdbcTemplate.update(DELETE_BY_TAG_ID, tagId);
    }

    /**
     * Removes all records which connected with specified certificate
     *
     * @param certificateId - id of the certificate by which records will be removed
     */
    public void deleteByCertificateId(long certificateId) {
        jdbcTemplate.update(DELETE_BY_CERTIFICATE_ID, certificateId);
    }

    /**
     * Add interaction between specified tag and certificate in the storage
     *
     * @param tagId         - id of the specified tag
     * @param certificateId - id of the specified certificate
     */
    public void addCertificateTag(long tagId, long certificateId) {
        jdbcTemplate.update(ADD_CERTIFICATE_TAG, tagId, certificateId);
    }
}

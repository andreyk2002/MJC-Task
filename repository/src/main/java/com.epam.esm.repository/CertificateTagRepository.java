package com.epam.esm.repository;

import com.epam.esm.entity.GiftTag;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class CertificateTagRepository {

    private static final String FIND_BY_CERTIFICATE = "SELECT * FROM certificate_tag ct " +
            "JOIN tag t ON ct.tag_id = t.id WHERE ct.certificate_id = ?";
    private static final String DELETE_BY_CERTIFICATE_ID = "DELETE FROM certificate_tag WHERE certificate_id = ?";
    private static final String DELETE_BY_TAG_ID = "DELETE FROM certificate_tag WHERE tag_id = ?";
    public static final String ADD_CERTIFICATE_TAG = "INSERT INTO certificate_tag (tag_id, certifcate_id) VALUES (? , ?)";
    private final JdbcTemplate jdbcTemplate;
    private final TagRowMapper tagRowMapper;


    public List<GiftTag> getTagByCertificateId(long id) {
        return jdbcTemplate.query(FIND_BY_CERTIFICATE, tagRowMapper, id);
    }

    public void deleteByTagId(long tagId) {
        jdbcTemplate.update(DELETE_BY_TAG_ID, tagId);
    }

    public void deleteByCertificateId(long certificateId){
        jdbcTemplate.update(DELETE_BY_CERTIFICATE_ID, certificateId);
    }

    public void addCertificateTag(long id, long tagId) {
        jdbcTemplate.update(ADD_CERTIFICATE_TAG);
    }
}

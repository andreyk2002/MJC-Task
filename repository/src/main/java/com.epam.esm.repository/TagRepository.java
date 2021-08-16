package com.epam.esm.repository;

import com.epam.esm.entity.GiftTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TagRepository {
    private static final String DELETE_QUERY = "DELETE FROM tag WHERE id = ?";
    private static final String SAVE_QUERY = "INSERT INTO tag (name) VALUES (?);";
    private static final String DELETE_ALL_QUERY = "DELETE FROM tag WHERE id > 0";
    private static final String FIND_BY_ID = "SELECT * FROM tag WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM tag";


    private final JdbcTemplate jdbcTemplate;
    private final TagRowMapper tagRowMapper;

    @Autowired
    public TagRepository(JdbcTemplate jdbcTemplate, TagRowMapper tagRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.tagRowMapper = tagRowMapper;
    }

    public int saveTag(GiftTag tag) {
        return jdbcTemplate.update(SAVE_QUERY, tag.getName());
    }

    public int deleteById(long tagId) {
        return jdbcTemplate.update(DELETE_QUERY, tagId);
    }


    public int deleteAll() {
        return jdbcTemplate.update(DELETE_ALL_QUERY);
    }

    public Optional<GiftTag> getById(long id) {
        try {
            GiftTag giftTag = jdbcTemplate.queryForObject(FIND_BY_ID, tagRowMapper, id);
            return Optional.of(giftTag);
        } catch (EmptyResultDataAccessException e) {
            //Do we need a logger
            return Optional.empty();
        }
    }


    public List<GiftTag> getAll() {
        return jdbcTemplate.query(FIND_ALL, tagRowMapper);
    }
}

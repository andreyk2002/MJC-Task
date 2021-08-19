package com.epam.esm.repository;

import com.epam.esm.entity.GiftTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class TagRepository {
    private static final String DELETE_QUERY = "DELETE FROM tag WHERE id = ?";
    private static final String SAVE_QUERY = "INSERT INTO tag (name) VALUES (?);";
    private static final String DELETE_ALL_QUERY = "DELETE FROM tag WHERE id > 0";
    private static final String FIND_BY_ID = "SELECT * FROM tag WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM tag";
    public static final String UPDATE_QUERY = "UPDATE tag SET name = ? WHERE = ?";


    private final JdbcTemplate jdbcTemplate;
    private final TagRowMapper tagRowMapper;

    @Autowired
    public TagRepository(JdbcTemplate jdbcTemplate, TagRowMapper tagRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.tagRowMapper = tagRowMapper;
    }

    public long addTag(GiftTag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                (connection) -> {
                    PreparedStatement statement =
                            connection.prepareStatement(SAVE_QUERY, new String[]{"name"});
                    statement.setString(1, tag.getName());
                    return statement;
                },
                keyHolder);
        Number key = keyHolder.getKey();
        return key.longValue();
    }

    public void updateTag(GiftTag giftTag) {
        jdbcTemplate.update(UPDATE_QUERY, giftTag.getName(), giftTag.getId());
    }

    public void deleteById(long tagId) {
        jdbcTemplate.update(DELETE_QUERY, tagId);
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

    public void deleteAll() {
        jdbcTemplate.update(DELETE_ALL_QUERY);
    }


    public List<GiftTag> getAll() {
        return jdbcTemplate.query(FIND_ALL, tagRowMapper);
    }
}

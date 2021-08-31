package com.epam.esm.repository.impl;

import com.epam.esm.entity.GiftTag;
import com.epam.esm.repository.TagRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

/**
 * TagRepository provides functionality for interaction with storage,
 * which contains data about {@link GiftTag} entities
 */
@AllArgsConstructor
@Repository
public class TagJdbcRepository implements TagRepository {
    private static final String DELETE_QUERY = "DELETE FROM tag WHERE id = ?";
    private static final String SAVE_QUERY = "INSERT INTO tag (name) VALUES (?);";
    private static final String FIND_BY_ID = "SELECT * FROM tag WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM tag";
    private static final String UPDATE_QUERY = "UPDATE tag SET name = ? WHERE id = ?";


    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<GiftTag> tagRowMapper;

    /**
     * Adds an instance of {@link GiftTag} into the storage
     *
     * @param tag instance of tag, needed to be added
     * @return ID of inserted tag
     */
    @Override
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

    /**
     * Updates an instance of {@link GiftTag} in the storage
     *
     * @param giftTag instance of tag, needed to be updated
     */
    @Override
    public void updateTag(GiftTag giftTag) {
        jdbcTemplate.update(UPDATE_QUERY, giftTag.getName(), giftTag.getId());
    }

    /**
     * Removes a tag with specified id from storage if present
     *
     * @param tagId - ID of tag to be removed
     */
    @Override
    public void deleteById(long tagId) {
        jdbcTemplate.update(DELETE_QUERY, tagId);
    }

    /**
     * Searches for tag with specified id in the storage
     *
     * @param id - ID of tag
     * @return instance of GiftTag wrapped with {@link Optional} if present,
     * else returns {@link Optional#empty()}
     */
    @Override
    public Optional<GiftTag> getById(long id) {
        try {
            GiftTag giftTag = jdbcTemplate.queryForObject(FIND_BY_ID, tagRowMapper, id);
            return Optional.of(giftTag);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * Returns list of all certificates, which are present in the storage
     *
     * @return List of all present tags
     */
    @Override
    public List<GiftTag> getAll() {
        return jdbcTemplate.query(FIND_ALL, tagRowMapper);
    }
}

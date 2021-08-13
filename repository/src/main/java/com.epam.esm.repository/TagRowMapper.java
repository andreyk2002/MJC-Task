package com.epam.esm.repository;

import com.epam.esm.entity.GiftTag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TagRowMapper implements RowMapper<GiftTag> {
    @Override
    public GiftTag mapRow(ResultSet resultSet, int i) throws SQLException {
        long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        return new GiftTag(id, name);
    }
}

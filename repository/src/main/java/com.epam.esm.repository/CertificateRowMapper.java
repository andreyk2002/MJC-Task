package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;


@Component
class CertificateRowMapper implements RowMapper<GiftCertificate> {
    @Override
    public GiftCertificate mapRow(ResultSet resultSet, int i) throws SQLException {
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        BigDecimal price = resultSet.getBigDecimal("price");
        long id = resultSet.getLong("id");
        Integer duration = resultSet.getInt("duration");
        LocalDateTime createDate = resultSet.getTimestamp("create_date").toLocalDateTime();
        LocalDateTime lastUpdateDate = resultSet.getTimestamp("last_update_date").toLocalDateTime();
        return GiftCertificate.builder()
                .id(id)
                .name(name)
                .price(price)
                .description(description)
                .duration(duration)
                .createDate(createDate)
                .lastUpdateDate(lastUpdateDate)
                .build();
    }
}

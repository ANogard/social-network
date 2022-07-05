package ru.example.socnetwork.model.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.example.socnetwork.model.entity.Country;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryMapper implements RowMapper<Country> {
    @Override
    public Country mapRow(ResultSet rs, int rowNum) throws SQLException {
        Country mapper = new Country();
        mapper.setName(rs.getString("country"));
        return mapper;
    }
}


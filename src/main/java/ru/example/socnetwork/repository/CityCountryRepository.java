package ru.example.socnetwork.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.example.socnetwork.model.entity.City;
import ru.example.socnetwork.model.entity.Country;
import ru.example.socnetwork.logging.DebugLogs;
import ru.example.socnetwork.model.mapper.CityMapper;
import ru.example.socnetwork.model.mapper.CountryMapper;

import java.util.List;

@RequiredArgsConstructor
@Repository
@DebugLogs
public class CityCountryRepository {

    private final JdbcTemplate jdbc;

    public List<City> getCityList() {
        String sql = "select city from person group by city";
        return jdbc.query(sql, new CityMapper());
    }

    public List<Country> getCountryList() {
        String sql = "select country from person group by country";
        return jdbc.query(sql, new CountryMapper());
    }
}

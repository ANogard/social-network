package ru.example.socnetwork.model.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.example.socnetwork.model.rsdto.FriendshipPersonDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FriendshipPersonMapper implements RowMapper<FriendshipPersonDto> {

    @Override
    public FriendshipPersonDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        FriendshipPersonDto mapper = new FriendshipPersonDto();
        mapper.setUserId(rs.getInt("user_id"));
        mapper.setStatus(rs.getString("status"));
        return mapper;
    }
}

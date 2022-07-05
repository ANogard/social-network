package ru.example.socnetwork.model.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.example.socnetwork.model.entity.Friendship;
import ru.example.socnetwork.model.entity.enums.TypeCode;

import java.sql.ResultSet;
import java.sql.SQLException;
public class FriendshipMapper implements RowMapper<Friendship> {

    @Override
    public Friendship mapRow(ResultSet rs, int rowNum) throws SQLException {
        Friendship mapper = new Friendship();
        mapper.setId(rs.getInt("id"));
        mapper.setSrcPersonId(rs.getInt("src_person_id"));
        mapper.setDstPersonId(rs.getInt("dst_person_id"));
        mapper.setTime(rs.getLong("time"));
        mapper.setCode(TypeCode.valueOf(rs.getString("code")));
        return mapper;
    }
}
package ru.example.socnetwork.model.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.example.socnetwork.model.entity.DeletedUser;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DeletedUserMapper implements RowMapper<DeletedUser> {

  @Override
  public DeletedUser mapRow(ResultSet rs, int rowNum) throws SQLException{
    DeletedUser user = new DeletedUser();
    user.setId(rs.getInt("id"));
    user.setPersonId(rs.getInt("person_id"));
    user.setFirstName(rs.getString("first_name"));
    user.setLastName(rs.getString("last_name"));
    user.setPhoto(rs.getString("photo"));
    user.setExpire(rs.getLong("expire"));
    return user;
  }
}

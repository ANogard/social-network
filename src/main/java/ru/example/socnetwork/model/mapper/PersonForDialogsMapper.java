package ru.example.socnetwork.model.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.example.socnetwork.model.rsdto.PersonForDialogsDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonForDialogsMapper implements RowMapper<PersonForDialogsDto> {
  @Override
  public PersonForDialogsDto mapRow(ResultSet rs, int rowNum) throws SQLException {
    PersonForDialogsDto person = new PersonForDialogsDto();

    person.setId(rs.getInt("id"));
    person.setPhoto(rs.getString("photo"));
    person.setFirstName(rs.getString("first_name"));
    person.setLastName(rs.getString("last_name"));
    person.setEMail(rs.getString("e_mail"));
    person.setLastOnlineTime(rs.getLong("last_online_time"));

    return person;
  }
}

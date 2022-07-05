package ru.example.socnetwork.model.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.example.socnetwork.model.rsdto.DialogDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RecipientMapper implements RowMapper<DialogDto> {
  @Override
  public DialogDto mapRow(ResultSet rs, int rowNum) throws SQLException {
    DialogDto dialogDto = new DialogDto();

    dialogDto.setId(rs.getInt("recipient_id"));

    return dialogDto;
  }
}

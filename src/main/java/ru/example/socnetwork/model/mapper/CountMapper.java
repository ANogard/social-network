package ru.example.socnetwork.model.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.example.socnetwork.model.rsdto.DialogsDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CountMapper implements RowMapper<DialogsDto> {

    @Override
    public DialogsDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        DialogsDto dialogsDto = new DialogsDto();

        dialogsDto.setCount(rs.getInt("unread_count"));

        return dialogsDto;
    }
}

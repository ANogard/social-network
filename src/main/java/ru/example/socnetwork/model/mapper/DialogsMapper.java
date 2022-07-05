package ru.example.socnetwork.model.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.example.socnetwork.model.rsdto.DialogDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DialogsMapper implements RowMapper<DialogDto> {
    @Override
    public DialogDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        DialogDto dialogDto = new DialogDto();

        dialogDto.setDialogId(rs.getInt("dialog_id"));
        dialogDto.setTime(rs.getLong("time"));
        dialogDto.setUnreadCount(rs.getInt("unread_count"));
        dialogDto.setMessageText(rs.getString("message_text"));
        dialogDto.setAuthorId(rs.getInt("last_author_id"));
        dialogDto.setReadStatus(rs.getString("read_status"));
        dialogDto.setMessageId(rs.getInt("message_id"));

        return dialogDto;
    }
}

package ru.example.socnetwork.model.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.example.socnetwork.model.rsdto.MessageDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageMapper implements RowMapper<MessageDto> {
    @Override
    public MessageDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        MessageDto mapper = new MessageDto();
        mapper.setId(rs.getInt("id"));
        mapper.setTime(rs.getLong("time"));
        mapper.setAuthorId(rs.getInt("author_id"));
        mapper.setRecipientId(rs.getInt("recipient_id"));
        mapper.setMessageText(rs.getString("message_text"));
        mapper.setReadStatus(rs.getString("read_status"));

        return mapper;
    }
}
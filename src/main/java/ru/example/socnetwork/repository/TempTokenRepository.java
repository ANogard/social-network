package ru.example.socnetwork.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.example.socnetwork.logging.DebugLogs;
import ru.example.socnetwork.model.entity.TempToken;
import ru.example.socnetwork.model.mapper.TempTokenMapper;

@Repository
@RequiredArgsConstructor
@DebugLogs
public class TempTokenRepository {
  private final JdbcTemplate jdbc;

  public void addToken(TempToken token) {
    String sql = "INSERT INTO temptoken (token, email) VALUES (?, ?)";
    jdbc.update(sql, token.getToken(), token.getEmail());
  }

  public TempToken getToken(String token) {
    String sql = "SELECT * FROM temptoken WHERE token = ?";
    return jdbc.queryForObject(sql, new TempTokenMapper(), token);
  }

  public void deleteToken(String token) {
    String sql = "DELETE FROM temptoken WHERE token = ?";
    jdbc.update(sql, token);
  }
}

package ru.example.socnetwork.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.socnetwork.logging.DebugLogs;
import ru.example.socnetwork.model.entity.TempToken;
import ru.example.socnetwork.repository.TempTokenRepository;

@Service
@RequiredArgsConstructor
@DebugLogs
public class TempTokenService {

  private final TempTokenRepository tempTokenRepository;

  public TempToken getToken(String token) {
    return tempTokenRepository.getToken(token);
  }

  public void deleteToken(String token) {
    tempTokenRepository.deleteToken(token);
  }

  public void addToken(TempToken token) {
    tempTokenRepository.addToken(token);
  }
}

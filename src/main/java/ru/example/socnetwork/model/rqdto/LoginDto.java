package ru.example.socnetwork.model.rqdto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Email;

@Data
@Schema(description = "Данные пользователя для входа")
public class LoginDto {
  @Email
  @Schema(example = "some@mail.ru")
  private String email;
  @Schema(example = "12345678")
  private String password;

  public boolean checkPassword(String inDBPassword) {
    return new BCryptPasswordEncoder().matches(this.password, inDBPassword);
  }
}

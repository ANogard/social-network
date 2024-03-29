package ru.example.socnetwork.model.rsdto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.example.socnetwork.model.entity.NotificationSettings;
import ru.example.socnetwork.model.entity.enums.TypeNotificationCode;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationSettingsDto {
  @JsonProperty("type")
  private TypeNotificationCode notificationTypeCode;
  private Boolean enable;

  public NotificationSettingsDto(NotificationSettings notificationSettings) {
    this.notificationTypeCode = notificationSettings.getNotificationTypeCode();
    this.enable = notificationSettings.getEnable();
  }
}

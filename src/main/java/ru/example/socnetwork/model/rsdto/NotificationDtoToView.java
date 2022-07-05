package ru.example.socnetwork.model.rsdto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationDtoToView {
  private Integer id;
  private String info;
  @JsonProperty("entity_author")
  private PersonDto entityAuthor;
  @JsonProperty("event_type")
  private String notificationType;
  @JsonProperty("sent_time")
  private Long sentTime;

  public NotificationDtoToView(NotificationDto notificationDto, PersonDto entityAuthor, String info) {

    this.id = notificationDto.getId();
    this.sentTime = notificationDto.getSentTime();
    this.notificationType = notificationDto.getNotificationType().toString();
    this.entityAuthor = entityAuthor;
    this.info = info;
  }
}

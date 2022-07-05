package ru.example.socnetwork.model.entity;

import lombok.Data;
import ru.example.socnetwork.model.entity.enums.TypeNotificationCode;

@Data
public class NotificationType {
  private Integer id;
  private TypeNotificationCode code;
  private String name;
}

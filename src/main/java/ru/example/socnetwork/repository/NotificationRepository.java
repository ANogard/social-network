package ru.example.socnetwork.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.example.socnetwork.logging.DebugLogs;
import ru.example.socnetwork.model.entity.Notification;
import ru.example.socnetwork.model.entity.enums.TypeReadStatus;
import ru.example.socnetwork.model.mapper.NotificationMapper;
import ru.example.socnetwork.model.rsdto.NotificationDto;

import java.util.List;

@RequiredArgsConstructor
@Repository
@DebugLogs
public class NotificationRepository {
  private final JdbcTemplate jdbc;

  public void deleteAllPersonNotifications(Integer personId) {

    String sql = "delete from notification where person_id = ?";
    jdbc.update(sql, personId);
  }

  public void addNotification(NotificationDto notificationDto) {

    String sql = "insert into notification (notification_type, sent_time, person_id, entity_id, dist_user_id, status, title)" +
            " values ((CAST(? AS notification_code_type)), ?, ?, ?, ?, (CAST(? AS read_status_type)), ?)";

    jdbc.update(sql, notificationDto.getNotificationType().toString(), notificationDto.getSentTime(),
            notificationDto.getPersonId(), notificationDto.getEntityId(), notificationDto.getDistUserId(),
            notificationDto.getStatus().toString(), notificationDto.getTitle());
  }

  public List<Notification> getAllNotificationsForPerson(int personId) {

    String sql = "select * from notification where dist_user_id = ? and status = (CAST(? AS read_status_type))";
    return jdbc.query(sql, new NotificationMapper(), personId, TypeReadStatus.SENT.toString());
  }

  public void readAllNotificationByUser(int personId) {

    String sql = "UPDATE notification SET status = CAST(? AS read_status_type) WHERE dist_user_id = ?";
    jdbc.update(sql, TypeReadStatus.READ.toString(), personId);
  }

  public void readUsersNotificationById(int notificationId) {

    String sql = "UPDATE notification SET status = CAST(? AS read_status_type) WHERE id = ?";
    jdbc.update(sql, TypeReadStatus.READ.toString(), notificationId);
  }

  public List<Notification> findNotification(Integer personId, Integer distUserId) {

    String sql = "select * from notification where notification_type = 'FRIEND_BIRTHDAY' and " +
            "person_id = ? and  dist_user_id = ?";
    return jdbc.query(sql, new NotificationMapper(), personId, distUserId);
  }

  public void deleteOldNotifications(Long time) {
    String sql = "delete from notification where sent_time < ? and status = 'READ'";
    jdbc.update(sql, time);
  }

  public List<Notification> chooseAllBirthdayNotifications() {
    String sql = "select * from notification where notification_type = 'FRIEND_BIRTHDAY'";
    return jdbc.query(sql, new NotificationMapper());
  }


  public void deleteNotificationById(Integer id) {
    String sql = "delete from notification where id = ?";
    jdbc.update(sql, id);
  }
}

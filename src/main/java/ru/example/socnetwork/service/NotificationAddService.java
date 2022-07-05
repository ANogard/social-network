package ru.example.socnetwork.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.socnetwork.model.entity.Notification;
import ru.example.socnetwork.model.rsdto.NotificationDto;
import ru.example.socnetwork.repository.NotificationRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class NotificationAddService {
  private final NotificationRepository notificationRepository;

  private final SecurityPerson securityPerson = new SecurityPerson();


  public void addNotificationForOnePerson(NotificationDto notificationDto) {
    notificationRepository.addNotification(notificationDto);
  }

  public List<Notification> getAllNotifications(Integer currentUserId) {
    return notificationRepository.getAllNotificationsForPerson(currentUserId);
  }

  public void readAllNotifications(int id, boolean all) {

    if (all) {
      notificationRepository.readAllNotificationByUser(securityPerson.getPersonId());
    } else {
      notificationRepository.readUsersNotificationById(id);
    }
  }

  public boolean checkIfNotificationNotExist(Integer personId, Integer distUserId) {
    return (notificationRepository.findNotification(personId, distUserId).size() == 0);
  }

  public void deleteOldNotifications(Long time) {
    notificationRepository.deleteOldNotifications(time);
  }

  public List<Notification> getAllBirthdaysNotifications() {
    return notificationRepository.chooseAllBirthdayNotifications();
  }

  public void deleteNotificationById(Integer id) {
    notificationRepository.deleteNotificationById(id);
  }

}
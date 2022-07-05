package ru.example.socnetwork.service;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.management.timer.Timer;

@Service
@EnableScheduling
@AllArgsConstructor
public class SchedulerService {
  private final NotificationService notificationService;
  private final SecurityPerson securityPerson = new SecurityPerson();

  @Scheduled(fixedRate = Timer.ONE_DAY)
  public void checkIfBirthdayOfFriends() {
    notificationService.checkAllBirthdays();
    notificationService.deleteOldNotifications();
    notificationService.deleteNotActualBirthdayNotification();
  }

}

package ru.example.socnetwork.service;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import ru.example.socnetwork.logging.InfoLogs;
import ru.example.socnetwork.model.entity.Notification;
import ru.example.socnetwork.model.entity.enums.TypeNotificationCode;
import ru.example.socnetwork.model.rsdto.NotificationDto;
import ru.example.socnetwork.model.rsdto.NotificationDtoToView;
import ru.example.socnetwork.model.rsdto.PersonDto;
import ru.example.socnetwork.repository.NotificationSettingsRepository;
import ru.example.socnetwork.repository.PersonRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@EnableScheduling
@InfoLogs
public class NotificationService {

  private final NotificationAddService notificationAddService;
  private final PersonService personService;
  private final FriendsService friendsService;
  private final NotificationSettingsRepository notificationSettingsRepository;
  private final PersonRepository personRepository;

  private final SecurityPerson securityPerson = new SecurityPerson();

  public void addNotificationForFriends(NotificationDto notificationDto) {

    List<PersonDto> friends = friendsService.getUserFriends();
    for (PersonDto friend : friends) {
      addNotificationForOnePerson(notificationDto, friend.getId());
    }
  }

  public void addNotificationForOnePerson(NotificationDto notificationDto,
                                          Integer destinationId) {

    boolean settingsEnable = notificationSettingsRepository.checkSettingsForNotification
            (notificationDto.getNotificationType(), destinationId);
    if (settingsEnable) {
      notificationDto.setDistUserId(destinationId);
      notificationAddService.addNotificationForOnePerson(notificationDto);
    }

  }

  public List<NotificationDtoToView> getAllNotifications() {

    personService.addOnlinePerson(securityPerson.getPersonId());

    List<Notification> notifications = notificationAddService.getAllNotifications(securityPerson.getPersonId());
    return notificationsToDto(notifications);
  }

  private List<NotificationDtoToView> notificationsToDto(List<Notification> notifications) {
    List<NotificationDto> notificationsDto = new ArrayList<>();
    for (Notification notification : notifications) {
      notificationsDto.add(new NotificationDto(notification));
    }

    return notificationDtoToNotificationViews(notificationsDto);
  }

  private List<NotificationDtoToView> notificationDtoToNotificationViews(List<NotificationDto> notificationsDto) {

    List<NotificationDtoToView> notificationDtoToView = new ArrayList<>();
    for (NotificationDto notification : notificationsDto) {
      notificationDtoToView.add(new NotificationDtoToView(notification,
              new PersonDto(personService.getById(notification.getPersonId())),
              notification.getTitle()
      ));
    }
    return notificationDtoToView;
  }

  public void readAllNotifications(int id, boolean all) {
    notificationAddService.readAllNotifications(id, all);
  }

  public Integer getPersonId() {
    return securityPerson.getPersonId();
  }

  public void checkAllBirthdays() {
    List<PersonDto> allUsers = getAllPersons();
    for (PersonDto person : allUsers) {
      if (checkIfBirthDayToday(person.getId())) {
        createNotificationForAllFriends(person);
      }
    }
  }

  public List<PersonDto> getAllPersons() {
    return personRepository.getAll().stream()
            .map(PersonDto::new)
            .collect(Collectors.toList());
  }

  private boolean checkIfBirthDayToday(Integer personId) {

    long birthDayDate = personService.getPersonBirthDay(personId).getBirthDate();
    LocalDate birthDayDay = Instant.ofEpochMilli(birthDayDate).atZone(ZoneId.systemDefault())
            .toLocalDate();
    int dayOfBirthday = birthDayDay.getDayOfMonth();
    String monthOfBirthday = birthDayDay.getMonth().toString();

    LocalDate today = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.systemDefault())
            .toLocalDate();
    int dayOfYear = today.getDayOfMonth();
    String month = today.getMonth().toString();

    return (dayOfBirthday == dayOfYear) && (month.equals(monthOfBirthday));
  }

  private void createNotificationForAllFriends(PersonDto person) {

    List<PersonDto> friends = personRepository.getUserFriends(person.getEmail()).stream()
            .map(PersonDto::new)
            .collect(Collectors.toList());

    for (PersonDto friend : friends) {
      createNotificationAboutBirthdayFriend(person.getId(), friend.getId());
    }
  }

  private void createNotificationAboutBirthdayFriend(Integer birthdayPersonId, Integer personId) {
    boolean check = notificationAddService.checkIfNotificationNotExist(birthdayPersonId, personId);

    if (check) {
      NotificationDto notificationDto = new NotificationDto(
              TypeNotificationCode.FRIEND_BIRTHDAY, System.currentTimeMillis(), birthdayPersonId,
              "У вашего друга сегодня день рождения!");
      addNotificationForOnePerson(notificationDto, personId);
    }
  }

  public void deleteOldNotifications() {
    LocalDate today = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.systemDefault())
            .toLocalDate();
    today = today.minusDays(Constants.DAYS_KEEPING_NOTIFICATIONS_IN_DB);
    long time = today.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000;

    notificationAddService.deleteOldNotifications(time);
  }

  public void deleteNotActualBirthdayNotification() {
    List<NotificationDto> notifications = notificationAddService.getAllBirthdaysNotifications().stream()
            .map(NotificationDto::new)
            .collect(Collectors.toList());
    for (NotificationDto notification : notifications) {
      if (!checkIfBirthDayToday(notification.getPersonId())) {
        notificationAddService.deleteNotificationById(notification.getId());
      }
    }
  }

  public void addNotificationIfBirthdayToday(Integer personId, Integer destinationId) {
    if (checkIfBirthDayToday(personId)) {
      createNotificationAboutBirthdayFriend(personId, destinationId);
    }
  }

}

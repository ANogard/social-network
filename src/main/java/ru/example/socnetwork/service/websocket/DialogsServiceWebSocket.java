package ru.example.socnetwork.service.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.socnetwork.model.rsdto.DialogDto;
import ru.example.socnetwork.model.rsdto.MessageDto;
import ru.example.socnetwork.repository.DialogRepository;
import ru.example.socnetwork.repository.MessageRepository;
import ru.example.socnetwork.repository.PersonRepository;
import ru.example.socnetwork.service.SecurityPerson;

@Service
@RequiredArgsConstructor

public class DialogsServiceWebSocket {

  private final MessageRepository messageRepository;
  private final DialogRepository dialogRepository;
  private final PersonRepository personRepository;
  private final SecurityPerson securityPerson = new SecurityPerson();

  public MessageDto sendMessage(String messageRequest, Integer dialogId, String email) {
    Integer userId = personRepository.getIdByEmail(email);
    DialogDto recipient = dialogRepository.getRecipientIdByDialogIdAndAuthorId(dialogId, userId);
    Integer recipientDialogId = dialogRepository.getDialogIdByPerson(recipient.getId(), userId).getDialogId();
    Long time = System.currentTimeMillis();
    if (recipientDialogId == 0) {
      dialogRepository.createDialogForMessage(recipient.getId(), userId, dialogId);
    }
    Integer messageId = messageRepository.sendMessage(time, userId,
            recipient.getId(),
            messageRequest, dialogId);
    return new MessageDto(messageId, time, userId, recipient.getRecipientId(),
            messageRequest, "SENT");
  }
}

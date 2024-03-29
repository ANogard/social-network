package ru.example.socnetwork.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import ru.example.socnetwork.model.rqdto.MessageRequest;
import ru.example.socnetwork.model.rsdto.*;
import ru.example.socnetwork.repository.DialogRepository;
import ru.example.socnetwork.repository.MessageRepository;
import ru.example.socnetwork.logging.DebugLogs;
import ru.example.socnetwork.model.entity.enums.TypeNotificationCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@DebugLogs
public class DialogsService {

    private final MessageRepository messageRepository;
    private final DialogRepository dialogRepository;
    private final NotificationService notificationService;
    private final SecurityPerson securityPerson = new SecurityPerson();
    private static boolean dialogCheck = true;

    public DialogDto deleteDialogByById(Integer dialogId) {

        DialogDto dto = new DialogDto();
        dto.setId(dialogRepository.deleteDialog(dialogId, securityPerson.getPersonId()));
        return dto;
    }

    public DialogDto createDialog(List<Integer> userList) {
        DialogDto dialogDto = new DialogDto();
        Integer dialogId = 0;
        Integer dialogCount;
        Integer recipientId = userList.get(0);
        dialogCount = dialogRepository
                .dialogCountByAuthorIdAndRecipientId(recipientId, securityPerson.getPersonId()).getDialogId();
        if (dialogCount < 1) {
            if (dialogCheck) {
                dialogCheck = false;
                if (dialogRepository.getDialogCount() == 0) {
                dialogRepository.createInitialDialog(recipientId, securityPerson.getPersonId());
                    dialogId = 1;}
                else {dialogId = dialogRepository.createDialog(securityPerson.getPersonId(), recipientId);}
            } else {
                dialogId = dialogRepository.createDialog(securityPerson.getPersonId(), recipientId);
            }
        }
        if (dialogCount == 1) {
            dialogId = dialogRepository.createDialogForMessage(securityPerson.getPersonId(), recipientId,
                    dialogRepository.getDialogIdByPerson(recipientId, securityPerson.getPersonId()).getDialogId());
        }
        dialogDto.setId(dialogId);
        return dialogDto;
    }

    public MessageDto sendMessage(MessageRequest messageRequest, Integer dialogId) {

        Integer currentUser = securityPerson.getPersonId();
        DialogDto recipient = dialogRepository.getRecipientIdByDialogIdAndAuthorId(dialogId, currentUser);
        Integer recipientId = recipient.getId();
        Integer recipientDialogId = dialogRepository.getDialogIdByPerson(recipientId, currentUser).getDialogId();
        Long time = System.currentTimeMillis();
        if (recipientDialogId == 0) {
            dialogRepository.createDialogForMessage(recipientId, currentUser, dialogId);
        }
        Integer messageId = messageRepository.sendMessage(time, currentUser,
                recipientId, messageRequest.getMessageText(), dialogId);

        NotificationDto notificationDto = new NotificationDto(TypeNotificationCode.MESSAGE, time, currentUser,
                messageId, getShortString(messageRequest.getMessageText()));
        notificationService.addNotificationForOnePerson(notificationDto, recipientId);

        return new MessageDto(messageId, time, currentUser, recipient.getRecipientId(),
                messageRequest.getMessageText(), Constants.SENT);
    }

    private String getShortString(String title) {
        return title.length() > 30 ? title.substring(0, 30) + "..." : title;
    }

    public List<DialogsDto> getDialogs() {
        List<DialogDto> dialogList;
        dialogList = dialogRepository.getDialogList(securityPerson.getPersonId());
        DialogsDto dialogsDto;
        List<DialogsDto> dialogsDtoList = new ArrayList<>();
        PersonForDialogsDto recipient;
        PersonForDialogsDto author;

        for (DialogDto dto : dialogList) {
            try {
                recipient = dialogRepository.getRecipientBydialogId(dto.getDialogId(), securityPerson.getPersonId());
                author = dialogRepository.getAuthorByDialogId(dto.getDialogId(), securityPerson.getPersonId());

                boolean isSendByMe = Objects.equals(securityPerson.getPersonId(), dto.getAuthorId());
                dialogsDto = new DialogsDto();
                dialogsDto.setId(dto.getDialogId());
                dialogsDto.setRecipient(recipient);
                dialogsDto.setMessageDto(new MessageDto(dto.getMessageId(),
                        author, recipient, dto.getTime(),
                        isSendByMe, dto.getMessageText(), dto.getReadStatus()));
                dialogsDto.setUnreadCount(dto.getUnreadCount());
                dialogsDtoList.add(dialogsDto);
            } catch (IncorrectResultSizeDataAccessException ignored) {}
        }

        return dialogsDtoList;
    }

    public List<MessageDto> getMessageDtoListByDialogId(Integer id) {

        List<MessageDto> messageList = messageRepository.getMessageList(id);

        if (messageList.stream().anyMatch(a -> a.getReadStatus().equals(Constants.SENT))) {
            messageRepository.updateReadStatus(id);
        }
        List<MessageDto> messageDtoList = new ArrayList<>();
        MessageDto messageDto;
        for (MessageDto dto : messageList) {
            boolean isSendByMe = Objects.equals(securityPerson.getPersonId(), dto.getAuthorId());
            messageDto = new MessageDto();
            PersonForDialogsDto recipient;
            PersonForDialogsDto author;

            if (isSendByMe) {
                recipient = messageRepository.getPersonForDialog(dto.getRecipientId());
                author = messageRepository.getPersonForDialog(dto.getAuthorId());
            } else {
                author = messageRepository.getPersonForDialog(dto.getAuthorId());
                recipient = messageRepository.getPersonForDialog(dto.getRecipientId());
            }
            messageDto.setId(dto.getId());
            messageDto.setAuthor(author);
            messageDto.setRecipient(recipient);
            messageDto.setTime(dto.getTime());
            messageDto.setSentByMe(isSendByMe);
            messageDto.setMessageText(dto.getMessageText());
            messageDto.setReadStatus(dto.getReadStatus());
            messageDtoList.add(messageDto);
        }
        return messageDtoList;
    }

    public DialogsDto getUnreadMessageCount() {
        return messageRepository.getUnreadCount(securityPerson.getPersonId());
    }
}

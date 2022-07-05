package ru.example.socnetwork.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.example.socnetwork.exception.ErrorResponseDto;
import ru.example.socnetwork.logging.InfoLogs;
import ru.example.socnetwork.model.rsdto.GeneralResponse;
import ru.example.socnetwork.model.rsdto.NotificationDtoToView;
import ru.example.socnetwork.service.NotificationService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/notifications")
@InfoLogs
@Tag(name = "notifications", description = "Взаимодействие с уведомлениями")
public class NotificationController {

  private final NotificationService notificationService;

  @GetMapping()
  @Operation(summary = "Получение списка уведомлений",
          responses = {
                  @ApiResponse(responseCode = "400", description = "Bad request",
                          content = @Content(mediaType = "application/json",
                                  array = @ArraySchema(
                                          schema = @Schema(implementation = ErrorResponseDto.class)
                                  ))),
                  @ApiResponse(responseCode = "401", description = "Unauthorized",
                          content = @Content(mediaType = "application/json",
                                  array = @ArraySchema(
                                          schema = @Schema(implementation = ErrorResponseDto.class)
                                  ))),
                  @ApiResponse(responseCode = "200", description = "Успешное получение списка уведомлений",
                          content = @Content(mediaType = "application/json",
                                  array = @ArraySchema(
                                          schema = @Schema(implementation = GeneralResponse.class)
                                  )))
          })
  public ResponseEntity<GeneralResponse<List<NotificationDtoToView>>> getNotificationsByUser(
          @RequestParam(value = "offset", defaultValue = "0", required = false) int offset,
          @RequestParam(value = "itemPerPage", defaultValue = "20", required = false) int perPage) {

    List<NotificationDtoToView> notifications = notificationService.getAllNotifications();
    GeneralResponse<List<NotificationDtoToView>> response = new GeneralResponse<>(notifications);

    return ResponseEntity.ok(response);
  }

  @PutMapping()
  @Operation(summary = "Отметить уведомление прочитанным",
          responses = {
                  @ApiResponse(responseCode = "400", description = "Bad request",
                          content = @Content(mediaType = "application/json",
                                  array = @ArraySchema(
                                          schema = @Schema(implementation = ErrorResponseDto.class)
                                  ))),
                  @ApiResponse(responseCode = "401", description = "Unauthorized",
                          content = @Content(mediaType = "application/json",
                                  array = @ArraySchema(
                                          schema = @Schema(implementation = ErrorResponseDto.class)
                                  ))),
                  @ApiResponse(responseCode = "200", description = "Успешная отметка",
                          content = @Content(mediaType = "application/json",
                                  array = @ArraySchema(
                                          schema = @Schema(implementation = GeneralResponse.class)
                                  )))
          })

  public ResponseEntity<GeneralResponse<Object>> getNotifications(
          @RequestParam(value = "id", defaultValue = "0", required = false) int id,
          @RequestParam(value = "all", defaultValue = "true", required = false) boolean all) {

    notificationService.readAllNotifications(id, all);
    return ResponseEntity.ok(new GeneralResponse<Object>());
  }

}

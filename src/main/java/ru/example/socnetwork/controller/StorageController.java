package ru.example.socnetwork.controller;

import com.dropbox.core.DbxException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.example.socnetwork.exception.ErrorResponseDto;
import ru.example.socnetwork.logging.InfoLogs;
import ru.example.socnetwork.model.rsdto.GeneralResponse;
import ru.example.socnetwork.model.rsdto.filedto.FileUploadDTO;
import ru.example.socnetwork.service.storage.StorageService;

import java.io.IOException;

@Log
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/storage")
@InfoLogs
@Tag(name = "storage", description = "Взаимодействие с хранилищем")
public class StorageController {

  private final StorageService storageService;

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Загрузка изображения", description = "Загрузка изображения в хранилище",
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
                  @ApiResponse(responseCode = "500", description = "Server error"),
                  @ApiResponse(responseCode = "200", description = "Успешная загрузка",
                          content = @Content(mediaType = "application/json",
                                  array = @ArraySchema(
                                          schema = @Schema(implementation = GeneralResponse.class)
                                  )))
          })
  public ResponseEntity<GeneralResponse<FileUploadDTO>> uploadImage(@RequestBody MultipartFile file)
          throws IOException, DbxException {

    return ResponseEntity.ok(new GeneralResponse<>(storageService.uploadFile(file), true));
  }

}

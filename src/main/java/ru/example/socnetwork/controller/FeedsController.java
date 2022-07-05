package ru.example.socnetwork.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.example.socnetwork.exception.ErrorResponseDto;
import ru.example.socnetwork.logging.InfoLogs;
import ru.example.socnetwork.model.rsdto.GeneralResponse;
import ru.example.socnetwork.model.rsdto.postdto.PostDto;
import ru.example.socnetwork.service.PostService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/feeds")
@InfoLogs
@Tag(name = "feeds", description = "Взаимодействие с лентой")
public class FeedsController {

  private final PostService postService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Получение списка новостей",
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
                  @ApiResponse(responseCode = "200", description = "Успешное получение списка новостей",
                          content = @Content(mediaType = "application/json",
                                  array = @ArraySchema(
                                          schema = @Schema(implementation = GeneralResponse.class)
                                  )))
          })
  public ResponseEntity<GeneralResponse<List<PostDto>>> getFeeds
          (@RequestParam(value = "offset", defaultValue = "0") int offset,
           @RequestParam(value = "perPage", defaultValue = "20") int perPage) {

    return ResponseEntity.ok(new GeneralResponse<>(postService.getFeeds(offset, perPage),
            postService.getPostCount(), offset, perPage));
  }
}

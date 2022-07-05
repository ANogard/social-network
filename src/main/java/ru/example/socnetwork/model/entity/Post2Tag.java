package ru.example.socnetwork.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Тег поста")
public class Post2Tag {
  private Integer id;
  @Schema(example = "13")
  private Integer tagId;
  @Schema(example = "13")
  private Integer postId;
}

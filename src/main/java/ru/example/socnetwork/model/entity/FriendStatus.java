package ru.example.socnetwork.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.example.socnetwork.model.entity.enums.TypeCode;

@Data
public class FriendStatus {
    @Schema(description = "Статус дружбы")
    private Integer id;
    @Schema(example = "1630627200000")
    private Long time;
    private String name;
    private TypeCode code;
}

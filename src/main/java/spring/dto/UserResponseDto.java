package spring.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Schema(description = "DTO пользователя, возвращаемый API")
@Getter
@AllArgsConstructor
public class UserResponseDto extends RepresentationModel<UserResponseDto> {

    @Schema(
            description = "Уникальный идентификатор пользователя"
    )
    private Long id;

    @Schema(
            description = "Имя пользователя"
    )
    private String name;

    @Schema(
            description = "Email пользователя"
    )
    private String email;

    @Schema(
            description = "Возраст пользователя"
    )
    private int age;

    @Schema(
            description = "Дата регистрации пользователя"
    )
    private LocalDateTime createdAt;
}

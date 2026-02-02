package spring.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Schema(description = "DTO для создания / обновления пользователя")
@Getter
@Setter
@NoArgsConstructor
public class UserRequestDto {

    @Schema(
            description = "Имя пользователя"
    )
    @NotBlank
    private String name;

    @Schema(
            description = "Email пользователя"
    )
    @NotBlank
    @Email
    private String email;

    @Schema(
            description = "Возраст пользователя",
            minimum = "0",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private int age;
}
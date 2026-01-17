package spring.dto;


import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UserRequestDto {

    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;

    private int age;
}
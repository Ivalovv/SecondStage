package spring.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserEventDto {
    private String email;
    private OperationType operation;

    public enum OperationType {
        CREATED,
        DELETED
    }
}

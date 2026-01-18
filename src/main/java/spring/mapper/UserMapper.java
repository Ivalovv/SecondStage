package spring.mapper;

import spring.model.User;
import org.springframework.stereotype.Component;
import spring.dto.UserRequestDto;
import spring.dto.UserResponseDto;

@Component
public class UserMapper {

    public User toEntity(UserRequestDto dto) {
        return new User(
                dto.getName(),
                dto.getEmail(),
                dto.getAge()
        );
    }

    public UserResponseDto toDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getCreatedAt()
        );
    }

    public void updateEntity(User user, UserRequestDto dto) {
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());
    }
}


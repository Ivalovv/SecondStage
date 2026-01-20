package spring.service;


import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import spring.dto.UserEventDto;
import spring.kafka.producer.UserEventProducer;
import spring.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.dto.UserRequestDto;
import spring.dto.UserResponseDto;
import spring.mapper.UserMapper;
import spring.repository.UserRepository;

import java.util.NoSuchElementException;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final UserEventProducer eventProducer;

    public UserServiceImpl(UserRepository userRepository, UserMapper mapper, UserEventProducer eventProducer) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.eventProducer = eventProducer;
    }

    @Override
    public UserResponseDto createUser(UserRequestDto dto) {
        User saved = userRepository.save(mapper.toEntity(dto));

        eventProducer.send(new UserEventDto(saved.getEmail(), UserEventDto.OperationType.CREATED));

        return mapper.toDto(saved);
    }

    @Override
    public UserResponseDto getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Пользователь не найден."));
        return mapper.toDto(user);
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto dto) {
        int updated = userRepository.updateUser(
                id,
                dto.getName(),
                dto.getEmail(),
                dto.getAge()
        );
        if (updated == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        User user = userRepository.findById(id)
                .orElseThrow();

        return mapper.toDto(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден."));

        userRepository.delete(user);

        eventProducer.send(new UserEventDto(user.getEmail(), UserEventDto.OperationType.DELETED));
    }
}

package spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import spring.dto.UserRequestDto;
import spring.dto.UserResponseDto;
import spring.service.UserService;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateUser() throws Exception {
        UserRequestDto request = new UserRequestDto();
        request.setName("Ivan");
        request.setEmail("ivan@test.com");
        request.setAge(25);

        UserResponseDto response = new UserResponseDto(
                1L,
                "Ivan",
                "ivan@test.com",
                25,
                LocalDateTime.now()
        );

        when(userService.createUser(any(UserRequestDto.class)))
                .thenReturn(response);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ivan"))
                .andExpect(jsonPath("$.email").value("ivan@test.com"))
                .andExpect(jsonPath("$.age").value(25));
    }
    @Test
    void shouldReturnUserById() throws Exception {
        UserResponseDto response = new UserResponseDto(
                1L,
                "Ivan",
                "ivan@test.com",
                25,
                LocalDateTime.now()
        );

        when(userService.getUser(1L)).thenReturn(response);

        mockMvc.perform(get("/users/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ivan"));
    }
    @Test
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/{id}", 1))
                .andExpect(status().isNoContent());
    }
    @Test
    void shouldUpdateUser() throws Exception {
        UserRequestDto request = new UserRequestDto();
        request.setName("NewName");
        request.setEmail("new@test.com");
        request.setAge(30);

        UserResponseDto response = new UserResponseDto(
                1L,
                "NewName",
                "new@test.com",
                30,
                LocalDateTime.now()
        );

        when(userService.updateUser(any(), any()))
                .thenReturn(response);

        mockMvc.perform(put("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("NewName"));
    }

    @Test
    void createUserNameIsNull() throws Exception {
        UserRequestDto request = new UserRequestDto();
        request.setName(null);
        request.setEmail("ivan@test.com");
        request.setAge(25);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserEmailIsNull() throws Exception {
        UserRequestDto request = new UserRequestDto();
        request.setName("Ivan");
        request.setEmail(null);
        request.setAge(25);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserEmailAlreadyExists() throws Exception {
        UserRequestDto request = new UserRequestDto();
        request.setName("Ivan");
        request.setEmail("ivan@test.com");
        request.setAge(25);

        when(userService.createUser(any(UserRequestDto.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists"));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(status().reason("Email already exists"));
    }

}

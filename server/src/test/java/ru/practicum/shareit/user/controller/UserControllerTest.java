package ru.practicum.shareit.user.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void getUsers() {
        ResponseEntity<Collection<UserDto>> responce = userController.getUsers();

        assertEquals(HttpStatus.OK, responce.getStatusCode());

    }


    @Test
    void getUser() {
        int userId = 0;

        ResponseEntity<UserDto> responce = userController.getUser(userId);

        assertEquals(HttpStatus.OK, responce.getStatusCode());

    }

    @Test
    void deleteUser() {
        int userId = 0;

        ResponseEntity<HttpStatus> responce = userController.deleteUser(userId);

        assertEquals(HttpStatus.OK, responce.getStatusCode());

    }

    @Test
    void createUser() {
        UserDto userDto = UserDto.builder().build();

        ResponseEntity<UserDto> responce = userController.createUser(userDto);

        assertEquals(HttpStatus.OK, responce.getStatusCode());

    }

    @Test
    void updateUser() {
        int userId = 0;
        UserDto userDto = UserDto.builder().build();

        ResponseEntity<UserDto> responce = userController.updateUser(userId, userDto);
        assertEquals(HttpStatus.OK, responce.getStatusCode());
    }
}



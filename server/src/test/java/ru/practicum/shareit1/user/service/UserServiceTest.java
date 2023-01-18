package ru.practicum.shareit1.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import ru.practicum.shareit.exceptions.NoSuchUserException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.mappers.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getUser_whenUserFound_thenReturnedUser() {
        int userId = 0;
        User expectedUser = new User();
        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.of(expectedUser));

        User actualUser = UserMapper.INSTANCE.userDtoToUser(userService.getUser(userId));
        assertEquals(expectedUser, actualUser);
    }


    @Test
    void getUser_whenUserNotFound_thenReturnedUserNotFoundExceptionThrown() {
        int userId = 0;
        User expectedUser = new User();
        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchUserException.class, () -> userService.getUser(userId));
    }

    @Test
    void createUser_whenUserNameValid_thenSavedUser() {

        User userToSave = new User();

        Mockito.when(userRepository.save(userToSave)).thenReturn(userToSave);

        User actualUser = UserMapper.INSTANCE.userDtoToUser(userService.createUser(UserMapper
                .INSTANCE.userToUserDto(userToSave)));

        assertEquals(userToSave, actualUser);
        Mockito.verify(userRepository).save(userToSave);
    }

    @Test
    void updateUser() {
        int userId = 0;
        User userToSave = new User();

        Mockito.when(userRepository.save(userToSave)).thenReturn(userToSave);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(userToSave));


        User actualUser = UserMapper.INSTANCE.userDtoToUser(userService.updateUser(UserMapper
                .INSTANCE.userToUserDto(userToSave), userId));

        assertEquals(userToSave, actualUser);
        Mockito.verify(userRepository).save(userToSave);
    }

    @Test
    void deleteUser() {
        int userId = 0;
        User userToSave = new User();

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(userToSave));
        HttpStatus httpStatus = userService.deleteUser(userId);

        assertEquals(HttpStatus.OK, httpStatus.OK);
        Mockito.verify(userRepository).delete(userToSave);
    }


    @Test
    void getUsers() {
        List<User> userListExpected = new ArrayList<>();
        User userToSave = new User();

        userListExpected.add(userToSave);

        Mockito.when(userRepository.findAll()).thenReturn(userListExpected);

        Collection<UserDto> actualUserList = userService.getUsers();

        assertEquals(UserMapper.INSTANCE.sourceListToTargetList(userListExpected), actualUserList);

    }


}
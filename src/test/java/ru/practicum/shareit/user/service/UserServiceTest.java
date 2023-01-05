package ru.practicum.shareit.user.service;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.NoSuchUserException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.mappers.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

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
        Assert.assertEquals(expectedUser, actualUser);
    }


    @Test
    void getUser_whenUserNotFound_thenReturnedUserNotFoundExceptionThrown() {
        int userId = 0;
        User expectedUser = new User();
        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        Assert.assertThrows(NoSuchUserException.class, () -> userService.getUser(userId));
    }

    @Test
    void createUser_whenUserNameValid_thenSavedUser() {
        int userId = 0;

        User userToSave = new User();

        Mockito.when(userRepository.save(userToSave)).thenReturn(userToSave);

        User actualUser = UserMapper.INSTANCE.userDtoToUser(userService.createUser(UserMapper
                .INSTANCE.userToUserDto(userToSave)));

        Assert.assertEquals(userToSave, actualUser);
        Mockito.verify(userRepository).save(userToSave);


    }


}
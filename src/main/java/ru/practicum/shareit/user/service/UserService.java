package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NoSuchUserException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dao.impl.UserDaoImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.mappers.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;

@Component
public class UserService {

    final UserDao userDao;
    int id = 1;


    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserDto createUser(UserDto user) {
        checkDoubleEmail(user);
        user.setId(generateId());
        return UserMapper.INSTANCE.userToUserDto(userDao.create(UserMapper.INSTANCE.userDtoToUser(user)));
    }


    public UserDto updateUser(UserDto user, int id) {

        if (userDao.findUserById(id).isPresent()) {
            checkDoubleEmail(user);
            user.setId(id);
            return UserMapper.INSTANCE.userToUserDto(userDao.update(UserMapper
                    .INSTANCE.updateUserFromDto(user, userDao.readOne(id)), id));
        } else {
            throw new NoSuchUserException("No User with such ID: " + id);
        }
    }

    public UserDto getUser(int id) {

        if (userDao.findUserById(id).isPresent()) {
            return UserMapper.INSTANCE.userToUserDto(userDao.readOne(id));
        } else {
            throw new NoSuchUserException("No User with such ID: " + id);
        }
    }


    public UserDto deleteUser(int id) {
        if (userDao.findUserById(id) != null) {
            userDao.delete(id);
            return UserMapper.INSTANCE.userToUserDto(userDao.readOne(id));
        } else {
            throw new NoSuchUserException("No User with such ID: " + id);
        }
    }

    public Collection<UserDto> getUsers() {
        return UserMapper.INSTANCE.sourceListToTargetList((List<User>) userDao.readAll());
    }

    private void checkDoubleEmail(UserDto user) {

        if (!userDao.users.values().stream()
                .noneMatch(p -> p.getEmail().equals(user.getEmail()))) {
            throw new ValidationException("double email");
        }
    }


    public int generateId() {
        return id++;
    }


}

package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NoSuchUserException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.mappers.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;

@Component
public class UserService {

    private final UserRepository userRepository;
    private int id = 1;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto createUser(UserDto user) {
        checkDoubleEmail(user);
//        user.setId(generateId());
        return UserMapper.INSTANCE.userToUserDto(userRepository.save(UserMapper.INSTANCE.userDtoToUser(user)));
    }


    public UserDto updateUser(UserDto user, int id) {

        if (userRepository.findById(id).isPresent()) {
            checkDoubleEmail(user);
            user.setId(id);

            return UserMapper.INSTANCE.userToUserDto(userRepository.save(UserMapper
                    .INSTANCE.updateUserFromDto(user, userRepository.findById(id).get())));
        } else {
            throw new NoSuchUserException("No User with such ID: " + id);
        }
    }
//
//    public UserDto getUser(int id) {
//
//        if (userRepository.findUserById(id).isPresent()) {
//            return UserMapper.INSTANCE.userToUserDto(userRepository.readOne(id));
//        } else {
//            throw new NoSuchUserException("No User with such ID: " + id);
//        }
//    }
//
//
//    public UserDto deleteUser(int id) {
//        if (userRepository.findUserById(id) != null) {
//            userRepository.delete(id);
//            return UserMapper.INSTANCE.userToUserDto(userRepository.readOne(id));
//        } else {
//            throw new NoSuchUserException("No User with such ID: " + id);
//        }
//    }
//
//    public Collection<UserDto> getUsers() {
//        return UserMapper.INSTANCE.sourceListToTargetList((List<User>) userRepository.readAll());
//    }
//
    private void checkDoubleEmail(UserDto user) {

        if (!userRepository.findAll().stream()
                .noneMatch(p -> p.getEmail().equals(user.getEmail()))) {
            throw new ValidationException("double email");
        }
    }


    public int generateId() {
        return id++;
    }


}

package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NoSuchUserException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.mappers.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor

public class UserService {

    private final UserRepository userRepository;


    public UserDto createUser(UserDto user) {
        return UserMapper.INSTANCE.userToUserDto(userRepository.save(UserMapper.INSTANCE.userDtoToUser(user)));
    }


    public UserDto updateUser(UserDto user, int id) {

        if (userRepository.findById(id).isPresent()) {
            user.setId(id);

            return UserMapper.INSTANCE.userToUserDto(userRepository.save(UserMapper
                    .INSTANCE.updateUserFromDto(user, userRepository.findById(id).get())));
        } else {
            throw new NoSuchUserException("No User with such ID: " + id);
        }
    }

    public UserDto getUser(int id) {

        userRepository.findById(id).orElseThrow(() -> new NoSuchUserException("No User with such ID: " + id));

        return UserMapper.INSTANCE.userToUserDto(userRepository.findById(id).get());
    }


    public void deleteUser(int id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.delete(userRepository.findById(id).get());
        } else {
            throw new NoSuchUserException("No User with such ID: " + id);
        }
    }


    public Collection<UserDto> getUsers() {
        return UserMapper.INSTANCE.sourceListToTargetList((List<User>) userRepository.findAll());
    }


}

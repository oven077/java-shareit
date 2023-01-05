package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@Slf4j
@RequestMapping(path = "/users")
@RequiredArgsConstructor

public class UserController {
    private final UserService userService;


    @PostMapping()
    public UserDto createUser(@Valid @RequestBody UserDto user) {
        log.info("controller:method userController -> createUser");

        return userService.createUser(user);
    }


    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable int id, @RequestBody UserDto user) {
        log.info("controller:method userController -> updateUser");

        return userService.updateUser(user, id);
    }


    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable int id) {
        log.info("controller:method userController -> getUser");

        return userService.getUser(id);
    }


    @GetMapping()
    public Collection<UserDto> getUsers() {
        log.info("controller:method userController -> getAllUsers");

        return userService.getUsers();

    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        log.info("controller:method userController -> deleteUser");
        userService.deleteUser(id);
    }
}

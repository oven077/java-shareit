package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.Collection;


@RestController
@Slf4j
@RequestMapping(path = "/users")
@RequiredArgsConstructor

public class UserController {
    private final UserService userService;


    @PostMapping()
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto user) {
        log.info("controller:method userController -> createUser");

        return ResponseEntity.ok(userService.createUser(user));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Integer id, @RequestBody UserDto user) {
        log.info("controller:method userController -> updateUser");

        return ResponseEntity.ok(userService.updateUser(user, id));
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Integer id) {
        log.info("controller:method userController -> getUser");

        return ResponseEntity.ok(userService.getUser(id));
    }


    @GetMapping()
    public ResponseEntity<Collection<UserDto>> getUsers() {
        log.info("controller:method userController -> getAllUsers");

        return ResponseEntity.ok(userService.getUsers());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Integer id) {
        log.info("controller:method userController -> deleteUser");
        return ResponseEntity.ok(userService.deleteUser(id));
    }
}

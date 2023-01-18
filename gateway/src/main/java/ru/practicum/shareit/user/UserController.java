package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@Slf4j
@RequestMapping(path = "/users")
@RequiredArgsConstructor

public class UserController {

    private final UserClient userClient;


    @PostMapping()
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto userDto) {
        log.info("controller:method userController -> createUser");

        return userClient.createUser(userDto);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable int id, @RequestBody UserDto userDto) {
        log.info("controller:method userController -> updateUser");

        return userClient.updateUser(userDto, id);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable int id) {
        log.info("controller:method userController -> getUser");

        return ResponseEntity.ok(userClient.getUser(id));
    }


    @GetMapping()
    public ResponseEntity<Object> getUsers() {
        log.info("controller:method userController -> getAllUsers");

        return ResponseEntity.ok(userClient.getUsers());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable int id) {
        log.info("controller:method userController -> deleteUser");
        return ResponseEntity.ok(userClient.deleteUser(id));
    }

}

package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

import javax.validation.Valid;

@Service
public class UserClient extends BaseClient {

    private static final String API_PREFIX = "/users";

    public UserClient(@Value("http://localhost:9090") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
    }

    public ResponseEntity<Object> createUser(@Valid UserDto userDto) {
        return post("", userDto);

    }

    public ResponseEntity<Object> updateUser(UserDto userDto, int id) {
        return patch("/" + id, userDto);
    }

    public ResponseEntity<Object> getUser(int id) {
        return get("/" + id);
    }

    public ResponseEntity<Object> getUsers() {
        return get("/");
    }

    public ResponseEntity<Object> deleteUser(int id) {
        return delete("/" + id);

    }
}
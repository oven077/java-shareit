package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    public ItemClient(@Value("http://localhost:9090") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
    }

    public ResponseEntity<Object> createItem(ItemDto itemDto, int userId) {
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> updateItem(ItemDto itemDto, int itemId, int userId) {
        return patch("/" + itemId, (long) userId, Map.of("itemId", itemId), itemDto);
    }

    public ResponseEntity<Object> getItemByIdWithBookings(int itemId, int userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getallitems(int userId) {
        return get("/", userId);
    }

    public ResponseEntity<Object> getAllItemsWithSearch(int userId, String text) {
        return get("/search?text=" + text, userId);
    }

    public ResponseEntity<Object> addCommentByItemId(int itemId, int userId, CommentDto commentDto) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }
}

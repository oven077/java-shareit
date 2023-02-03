package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class ItemRequestClient extends BaseClient {

    private static final String API_PREFIX = "/requests";

    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
    }

    public ResponseEntity<Object> createItemRequest(ItemRequestDto itemRequestDto, int userId) {
        return post("", userId, itemRequestDto);
    }

    public ResponseEntity<Object> getItemRequests(int userId, int page, int pageSize) {

        Map<String, Object> parameters = Map.of(
                "from", page,
                "size", pageSize
        );
        return get("/", (long) userId, parameters);
    }

    public ResponseEntity<Object> getItemRequestsAllOther(int userId, int page, int pageSize) {
        Map<String, Object> parameters = Map.of(
                "from", page,
                "size", pageSize
        );
        return get("/all?from={from}&size={size}", (long) userId, parameters);
    }

    public ResponseEntity<Object> getItemRequestById(int itemId, int userId) {
        return get("/" + itemId, userId);
    }
}

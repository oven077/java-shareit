package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@Slf4j
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping()
    public ResponseEntity<Object> createItemRequest(@Valid @RequestBody ItemRequestDto itemRequestDto, @RequestHeader("X-Sharer-User-Id") @Min(1) int userId) {
        log.info("controller:method ItemRequest -> createItemRequest");

        return itemRequestClient.createItemRequest(itemRequestDto, userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemRequestById(@PathVariable int id, @RequestHeader("X-Sharer-User-Id") @Min(1) int userId) {
        log.info("controller:method userController -> getUser");

        return itemRequestClient.getItemRequestById(id, userId);
    }


    @GetMapping()
    public ResponseEntity<Object> getItemRequests(@RequestHeader("X-Sharer-User-Id") @Min(1) int userId,
                                                  @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int page,
                                                  @RequestParam(value = "size", defaultValue = "10") @Positive int pageSize) {
        log.info("controller:method userController -> getAllUsers");
        return itemRequestClient.getItemRequests(userId, page, pageSize);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getItemRequestsAllOther(@RequestHeader("X-Sharer-User-Id") @Min(1) int userId,
                                                          @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int page,
                                                          @Positive @RequestParam(value = "size", defaultValue = "10") int pageSize) {
        log.info("controller:method userController -> getAllUsers");
        return itemRequestClient.getItemRequestsAllOther(userId, page, pageSize);
    }
}

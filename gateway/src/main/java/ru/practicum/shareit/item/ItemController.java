package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@Slf4j
@RequestMapping(path = "/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemClient itemClient;


    @PostMapping()
    public ResponseEntity<Object> createItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") @Min(1) int userId) {
        log.info("controller:method userController -> createUser");

        return itemClient.createItem(itemDto, userId);
    }


    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable int itemId, @RequestBody ItemDto item, @RequestHeader("X-Sharer-User-Id") @Min(1) int userId) {
        log.info("controller:method itemController -> updateItem");

        return itemClient.updateItem(item, itemId, userId);
    }


    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable int itemId,
                                               @RequestHeader("X-Sharer-User-Id") @Min(1) int userId) {
        log.info("controller:method itemController -> getItemById");

        return itemClient.getItemByIdWithBookings(itemId, userId);
    }

    @GetMapping()
    public ResponseEntity<Object> getallitems(@RequestHeader("X-Sharer-User-Id") @Min(1) int userId) {
        log.info("controller:method itemController -> getAllItems");

        return itemClient.getallitems(userId);
    }


    @GetMapping("/search")
    @Validated
    public ResponseEntity<Object> getAllItemsWithSearch(@Valid @RequestParam(defaultValue = "///") String text,
                                                                     @RequestHeader("X-Sharer-User-Id") @Min(1) int userId) {
        log.info("controller:method itemController -> getAllItemsWithSearch");

        return itemClient.getAllItemsWithSearch(userId, text);
    }


    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addCommentByItemId(@PathVariable int itemId,
                                                         @RequestHeader("X-Sharer-User-Id") @Min(1) int userId,
                                                         @RequestBody @Valid CommentDto commentDto) {
        log.info("controller:method itemController -> getItemById");

        return itemClient.addCommentByItemId(itemId, userId, commentDto);
    }
}

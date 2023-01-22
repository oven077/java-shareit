package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;


@RestController
@Slf4j
@RequestMapping("/items")
@RequiredArgsConstructor

public class ItemController {

    private final ItemService itemService;

    @PostMapping()
    public ResponseEntity<ItemDto> createItem(@Valid @RequestBody ItemDto item, @RequestHeader("X-Sharer-User-Id") @Min(1) Integer userId
    ) {
        log.info("controller:method itemController -> createItem");

        return ResponseEntity.ok(itemService.createItem(item, userId));
    }


    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable int itemId, @RequestBody ItemDto item, @RequestHeader("X-Sharer-User-Id") @Min(1) int userId) {
        log.info("controller:method itemController -> updateItem");

        return ResponseEntity.ok(itemService.updateItem(item, itemId, userId));
    }


    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> getItemById(@PathVariable int itemId,
                                               @RequestHeader("X-Sharer-User-Id") @Min(1) int userId) {
        log.info("controller:method itemController -> getItemById");

        return ResponseEntity.ok(itemService.getItemBookings(itemId, userId));
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> addCommentByItemId(@PathVariable int itemId,
                                                         @RequestHeader("X-Sharer-User-Id") @Min(1) int userId,
                                                         @RequestBody @Valid CommentDto commentDto) {
        log.info("controller:method itemController -> getItemById");

        return ResponseEntity.ok(itemService.addCommentByItemId(itemId, userId, commentDto));
    }


    @GetMapping()
    public ResponseEntity<Collection<ItemDto>> getAllItems(@RequestHeader("X-Sharer-User-Id") @Min(1) int userId) {
        log.info("controller:method itemController -> getAllItems");

        return ResponseEntity.ok(itemService.getAllItems(userId));
    }

    @GetMapping("/search")
    @Validated
    public ResponseEntity<Collection<ItemDto>> getAllItemsWithSearch(@Valid String text,
                                                                     @RequestHeader("X-Sharer-User-Id") @Min(1) int userId) {
        log.info("controller:method itemController -> getAllItemsWithSearch");

        return ResponseEntity.ok(itemService.getAllItemsWithSearch(userId, text));
    }
}

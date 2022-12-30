package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@Slf4j
@RequestMapping("/items")
@RequiredArgsConstructor

public class ItemController {

    private final ItemService itemService;

    @PostMapping()
    public ItemDto createItem(@Valid @RequestBody ItemDto item, @RequestHeader("X-Sharer-User-Id") @Min(1) int userId
    ) {
        log.info("controller:method itemController -> createItem");

        return itemService.createItem(item, userId);
    }


    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable int itemId, @RequestBody ItemDto item, @RequestHeader("X-Sharer-User-Id") @Min(1) int userId) {
        log.info("controller:method itemController -> updateItem");

        return itemService.updateItem(item, itemId, userId);


    }


    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable int itemId,
                               @RequestHeader("X-Sharer-User-Id") @Min(1) int userId) {
        log.info("controller:method itemController -> getItemById");

        return itemService.getItemBookings(itemId, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addCommentByItemId(@PathVariable int itemId,
                                         @RequestHeader("X-Sharer-User-Id") @Min(1) int userId,
                                         @RequestBody @Valid CommentDto commentDto) {
        log.info("controller:method itemController -> getItemById");

        return itemService.addCommentByItemId(itemId, userId, commentDto);
    }


    @GetMapping()
    public Collection<ItemDto> getAllItems(@RequestHeader("X-Sharer-User-Id") @Min(1) int userId) {
        log.info("controller:method itemController -> getAllItems");

        return itemService.getAllItems(userId);
    }

    @GetMapping("/search")
    @Validated
    public Collection<ItemDto> getAllItemsWithSearch(@Valid @RequestParam(defaultValue = "///") String text,
                                                     @RequestHeader("X-Sharer-User-Id") @Min(1) int userId) {
        log.info("controller:method itemController -> getAllItemsWithSearch");

        return itemService.getAllItemsWithSearch(userId, text);
    }
}

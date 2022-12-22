package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.error.AppError;
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
    public ResponseEntity<?> createItem(@Valid @RequestBody ItemDto item, @RequestHeader("X-Sharer-User-Id") @Min(1) int userId
    ) {
        log.info("controller:method itemController -> createItem");
        try {
            ItemDto itemDto = itemService.createItem(item, userId);
            return new ResponseEntity<>(itemDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                    "Could not create item " + item),
                    HttpStatus.NOT_FOUND);
        }
    }


    @PatchMapping("/{itemId}")
    public ResponseEntity<?> updateItem(@PathVariable int itemId, @RequestBody ItemDto item, @RequestHeader("X-Sharer-User-Id") @Min(1) int userId) {
        log.info("controller:method itemController -> updateItem");
        ItemDto itemDto = itemService.updateItem(item, itemId, userId);
        return new ResponseEntity<>(itemDto, HttpStatus.OK);

    }


    @GetMapping("/{itemId}")
    public ResponseEntity<?> getItemById(@PathVariable int itemId) {
        log.info("controller:method itemController -> getItemById");
        try {
            ItemDto item = itemService.getItem(itemId);
            return new ResponseEntity<>(item, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                    "Item with id " + itemId + " not found"),
                    HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping()
    public ResponseEntity<?> getAllItems(@RequestHeader("X-Sharer-User-Id") @Min(1) int userId) {
        log.info("controller:method itemController -> getAllItems");
        try {
            Collection<ItemDto> listItems = itemService.getAllItems(userId);
            return new ResponseEntity<>(listItems, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                    "Empty list of items"),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    @Validated
    public ResponseEntity<?> getAllItemsWithSearch(@Valid @RequestParam(defaultValue = "///") String text,
                                                   @RequestHeader("X-Sharer-User-Id") @Min(1) int userId) {
        log.info("controller:method itemController -> getAllItemsWithSearch");

        try {
            Collection<ItemDto> listItems = itemService.getAllItemsWithSearch(userId, text);
            return new ResponseEntity<>(listItems, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                    "Not match with filter"),
                    HttpStatus.NOT_FOUND);
        }
    }
}

package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@Slf4j
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping()
    public ItemRequestDto createItemRequest(@Valid @RequestBody ItemRequestDto itemRequestDto, @RequestHeader("X-Sharer-User-Id") @Min(1) int userId) {
        log.info("controller:method ItemRequest -> createItemRequest");

        return itemRequestService.createItemRequest(itemRequestDto, userId);
    }

    //
//    @PatchMapping("/{id}")
//    public UserDto updateUser(@PathVariable int id, @RequestBody UserDto user) {
//        log.info("controller:method userController -> updateUser");
//
//        return userService.updateUser(user, id);
//    }


    @GetMapping("/{id}")
    public ItemRequestDto getItemRequestDtoById(@PathVariable int id, @RequestHeader("X-Sharer-User-Id") @Min(1) int userId) {
        log.info("controller:method userController -> getUser");

        return itemRequestService.getItemRequestDtoById(id, userId);
    }


    @GetMapping()
    public List<ItemRequestDto> getItemRequests(@RequestHeader("X-Sharer-User-Id") @Min(1) int userId,
                                                @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int page,
                                                @Positive @RequestParam(value = "size", defaultValue = "10") int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC,"created");
        final Pageable pageable = PageRequest.of(page,pageSize,sort);

        log.info("controller:method userController -> getAllUsers");

        return itemRequestService.getItemRequests(userId, pageable);

    }


    @GetMapping("/all")
    public List<ItemRequestDto> getItemRequestsAllOther(@RequestHeader("X-Sharer-User-Id") @Min(1) int userId,
                                                   @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int page,
                                                   @Positive @RequestParam(value = "size", defaultValue = "10") int pageSize) {

        Sort sort = Sort.by(Sort.Direction.DESC,"created");
        final Pageable pageable = PageRequest.of(page,pageSize,sort);

        log.info("controller:method userController -> getAllUsers");

        return itemRequestService.getItemRequestsAllOther(userId, pageable);

    }


//
//    @DeleteMapping("/{id}")
//    public void deleteUser(@PathVariable int id) {
//        log.info("controller:method userController -> deleteUser");
//        userService.deleteUser(id);
//    }


}

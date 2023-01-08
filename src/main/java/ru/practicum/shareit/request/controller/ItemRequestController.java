package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
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
    public ResponseEntity<ItemRequestDto> createItemRequest(@Valid @RequestBody ItemRequestDto itemRequestDto, @RequestHeader("X-Sharer-User-Id") @Min(1) int userId) {
        log.info("controller:method ItemRequest -> createItemRequest");

        return ResponseEntity.ok(itemRequestService.createItemRequest(itemRequestDto, userId));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ItemRequestDto> getItemRequestById(@PathVariable int id, @RequestHeader("X-Sharer-User-Id") @Min(1) int userId) {
        log.info("controller:method userController -> getUser");

        return ResponseEntity.ok(itemRequestService.getItemRequestById(id, userId));
    }


    @GetMapping()
    public ResponseEntity<Collection<ItemRequestDto>> getItemRequests(@RequestHeader("X-Sharer-User-Id") @Min(1) int userId,
                                                                      @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int page,
                                                                      @RequestParam(value = "size", defaultValue = "10") @Positive int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "created");
        final Pageable pageable = PageRequest.of(page, pageSize, sort);

        log.info("controller:method userController -> getAllUsers");

        return ResponseEntity.ok(itemRequestService.getItemRequests(userId, pageable));

    }


    @GetMapping("/all")
    public ResponseEntity<List<ItemRequestDto>> getItemRequestsAllOther(@RequestHeader("X-Sharer-User-Id") @Min(1) int userId,
                                                                        @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int page,
                                                                        @Positive @RequestParam(value = "size", defaultValue = "10") int pageSize) {

        Sort sort = Sort.by(Sort.Direction.DESC, "created");
        final Pageable pageable = PageRequest.of(page, pageSize, sort);

        log.info("controller:method userController -> getAllUsers");

        return ResponseEntity.ok(itemRequestService.getItemRequestsAllOther(userId, pageable));

    }

}

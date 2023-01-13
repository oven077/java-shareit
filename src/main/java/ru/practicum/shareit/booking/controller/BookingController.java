package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@Slf4j
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {


    private final BookingService bookingService;

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDto> setApproveOrRejectBooking(@PathVariable int bookingId, @Valid @RequestParam Boolean approved,
                                                                @RequestHeader("X-Sharer-User-Id") @Min(1) int userId) {
        log.info("controller:method itemController -> updateItem");
        return ResponseEntity.ok(bookingService.setApproveOrRejectBooking(bookingId, approved, userId));
    }

    @PostMapping()
    public ResponseEntity<BookingDto> createBooking(@Valid @RequestBody BookingDto bookingDto, @RequestHeader("X-Sharer-User-Id") @Min(1) int userId) {
        log.info("controller:method bookingController -> createBooking");
        return ResponseEntity.ok(bookingService.createBooking(bookingDto, userId));
    }


    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getBooking(@PathVariable int id,
                                                 @RequestHeader("X-Sharer-User-Id") @Min(1) int userId) {
        log.info("controller:method userController -> getUser");
        return ResponseEntity.ok(bookingService.getBooking(id, userId));
    }


    @GetMapping()
    public ResponseEntity<List<BookingDto>> getAll(@RequestHeader("X-Sharer-User-Id") @Min(1) int userId,
                                                   @RequestParam(value = "state", required = false) String state,
                                                   @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") @Min(0) int page,
                                                   @Positive @RequestParam(value = "size", defaultValue = "10") @Min(0) int pageSize
    ) {

        log.info("controller:method itemController -> getAllItems");
        return ResponseEntity.ok(bookingService.getAll(userId, state, page, pageSize));
    }


    @GetMapping("/owner")
    public ResponseEntity<List<BookingDto>> getOwnerItemsAll(@RequestHeader("X-Sharer-User-Id") @Min(1) int userId,
                                                             @RequestParam(value = "state", required = false) String state,
                                                             @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int page,
                                                             @Positive @RequestParam(value = "size", defaultValue = "10") int pageSize) {


        log.info("controller:method itemController -> getAllItems");
        return ResponseEntity.ok(bookingService.getOwnerItemsAll(userId, state, page, pageSize));
    }
}

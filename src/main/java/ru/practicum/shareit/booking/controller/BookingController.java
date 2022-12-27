package ru.practicum.shareit.booking.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.error.AppError;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.UnsupportedState;

import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@Slf4j
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<?> approveBooking(@PathVariable int bookingId, @Valid @RequestParam Boolean approved, @RequestHeader("X-Sharer-User-Id") @Min(1) int userId) {
        log.info("controller:method itemController -> updateItem");
        if(approved){
            return new ResponseEntity<>(bookingService.approveBooking(bookingId, approved, userId), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(bookingService.rejectBooking(bookingId, approved, userId), HttpStatus.OK);
        }
    }

    @PostMapping()
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingDto bookingDto, @RequestHeader("X-Sharer-User-Id") @Min(1) int userId) {
        log.info("controller:method bookingController -> createBooking");
        try {
            return new ResponseEntity<>(bookingService.createBooking(bookingDto, userId), HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(),
                    "Could not create item " + bookingDto),
                    HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                    "Could not create item " + bookingDto),
                    HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getBooking(@PathVariable int id,
                                        @RequestHeader("X-Sharer-User-Id") @Min(1) int userId) {
        log.info("controller:method userController -> getUser");
        try {
            return new ResponseEntity<>(bookingService.getBooking(id, userId), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                    "Could not get user by id" + id),
                    HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(),
                    "Could not get user by id" + id),
                    HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping()
    public ResponseEntity<?> getAll(@RequestHeader("X-Sharer-User-Id") @Min(1) int userId,
                                    @RequestParam(value = "state", required = false) String state) {
        log.info("controller:method itemController -> getAllItems");
        try {
            return new ResponseEntity<>(bookingService.getAll(userId, state), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                    "Empty list of items"),
                    HttpStatus.NOT_FOUND);
        } catch (UnsupportedState e) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(),
                    "Unknown state: UNSUPPORTED_STATUS"),
                    HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/owner")
    public ResponseEntity<?> getOwnerItemsAll(@RequestHeader("X-Sharer-User-Id") @Min(1) int userId,
                                              @RequestParam(value = "state", required = false) String state) {
        log.info("controller:method itemController -> getAllItems");
        try {
            return new ResponseEntity<>(bookingService.getOwnerItemsAll(userId, state), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                    "not found"),
                    HttpStatus.NOT_FOUND);
        } catch (UnsupportedState e) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(),
                    "Unknown state: UNSUPPORTED_STATUS"),
                    HttpStatus.BAD_REQUEST);
        }
    }

}

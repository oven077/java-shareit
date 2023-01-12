package ru.practicum.shareit.booking.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;


    @Test
    void setApproveOrRejectBooking() {
        int userId = 0;
        int bookingId = 0;
        BookingDto bookingDto = BookingDto.builder().build();

        Mockito.when(bookingService.setApproveOrRejectBooking(bookingId, true, userId)).thenReturn(bookingDto);
        ResponseEntity<BookingDto> responce = bookingController.setApproveOrRejectBooking(bookingId, true, userId);
        assertEquals(HttpStatus.OK, responce.getStatusCode());


    }

    @Test
    void createBooking() {
        int userId = 0;
        BookingDto bookingDto = BookingDto.builder().build();

        Mockito.when(bookingService.createBooking(bookingDto, userId)).thenReturn(bookingDto);
        ResponseEntity<BookingDto> responce = bookingController.createBooking(bookingDto, userId);
        assertEquals(HttpStatus.OK, responce.getStatusCode());
    }

    @Test
    void getBooking() {
        int userId = 0;
        int bookingId = 0;
        BookingDto bookingDto = BookingDto.builder().build();

        Mockito.when(bookingService.getBooking(bookingId, userId)).thenReturn(bookingDto);
        ResponseEntity<BookingDto> responce = bookingController.getBooking(bookingId, userId);
        assertEquals(HttpStatus.OK, responce.getStatusCode());
    }

    @Test
    void getAll() {
        int userId = 0;
        List<BookingDto> bookingDtoList = new ArrayList<>();

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        final Pageable pageable = PageRequest.of(0, 10, sort);


        Mockito.when(bookingService.getAll(userId, "state", pageable)).thenReturn(bookingDtoList);
        ResponseEntity<List<BookingDto>> responce = bookingController.getAll(userId, "state", 0, 10);
        assertEquals(HttpStatus.OK, responce.getStatusCode());
    }

    @Test
    void getOwnerItemsAll() {
        int userId = 0;
        List<BookingDto> bookingDtoList = new ArrayList<>();

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        final Pageable pageable = PageRequest.of(0, 10, sort);


        Mockito.when(bookingService.getOwnerItemsAll(userId, "state", pageable)).thenReturn(bookingDtoList);
        ResponseEntity<List<BookingDto>> responce = bookingController.getOwnerItemsAll(userId, "state", 0, 10);
        assertEquals(HttpStatus.OK, responce.getStatusCode());


    }
}
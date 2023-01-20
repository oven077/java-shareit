package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Builder
@Jacksonized
@Data
public class BookingDto {
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;

    private int itemId;
    private Item item;
    private User booker;
    private Status status;

    private int bookerId;

}

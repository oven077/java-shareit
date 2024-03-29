package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.comment.CommentDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


@Builder
@Jacksonized
@Data
public class ItemDto {

    private int id;

    @NotNull
    @NotBlank(message = "Invalid name")
    private String name;
    @NotNull
    @NotBlank(message = "Invalid description")
    private String description;

    @NotNull
    private Boolean available;
    private List<CommentDto> comments;

    private BookingDto lastBooking;
    private BookingDto nextBooking;

    private int requestId;
}

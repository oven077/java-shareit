package ru.practicum.shareit.item;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
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
//    private List<CommentDto> comments;
//
//    private BookingDto lastBooking;
//    private BookingDto nextBooking;

    private int requestId;

}

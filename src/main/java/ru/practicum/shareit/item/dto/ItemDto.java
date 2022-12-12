package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */

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

}

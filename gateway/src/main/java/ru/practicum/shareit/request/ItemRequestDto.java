package ru.practicum.shareit.request;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.user.UserDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Builder
@Jacksonized
@Data
public class ItemRequestDto {

    public int id;
    @NotNull
    public String description;
    public UserDto requestor;
    private LocalDateTime created;

    private List<ItemDto> items;


}

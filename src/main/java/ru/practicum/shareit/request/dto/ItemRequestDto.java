package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequestDto {

    public int id;
    @NotNull
    public String description;
    public UserDto requestor;
    private LocalDateTime created;

    private List<ItemDto> items;



}

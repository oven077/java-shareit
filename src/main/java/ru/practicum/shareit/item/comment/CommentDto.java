package ru.practicum.shareit.item.comment;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {
    private int id;
    @NotBlank
    @NotEmpty
    private String text;

    private Item item;
    private String authorName;


    private User author;
    private LocalDateTime created;


}

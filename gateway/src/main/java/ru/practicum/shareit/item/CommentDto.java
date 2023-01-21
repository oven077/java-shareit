package ru.practicum.shareit.item;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Builder
@Jacksonized
@Data
public class CommentDto {
    private int id;
    @NotBlank
    @NotEmpty
    private String text;

    //    private Item item;
    private String authorName;


    //    private User author;
    private LocalDateTime created;


}

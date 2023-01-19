package ru.practicum.shareit.user.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Data
public class UserDto {
    private int id;
    @NotNull
    @NotBlank(message = "Invalid name")
    private String name;
    @NotNull
    @Email
    @Schema(type = "string", example = "test@test.com")
    private String email;
}

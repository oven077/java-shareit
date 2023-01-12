package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIT {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookingController bookingController;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void createUser() {
    }

    @Test
    void updateUser() {
    }

    @SneakyThrows
    @Test
    void getUser() {
        int userId = 0;
        UserDto userActual = UserDto.builder().build();
        userActual.setId(1);
        userActual.setName("asd");
        userActual.setEmail("asd@asd.ru");

        when(userService.getUser(userId)).thenReturn(userActual);


        String result = mockMvc.perform(get("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userActual)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userActual), result);

        verify(userService).getUser(userId);
    }
}
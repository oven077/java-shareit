package ru.practicum.shareit.item.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;

@SpringBootTest(properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
//@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureMockMvc
class ItemServiceIT {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookingController bookingController;

    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Test
    void getAllItems() {
        ItemDto itemDto = new ItemDto();
        int userId = 1;

        UserDto userDto = new UserDto();
        userDto.setId(userId);
        userDto.setName("asd");
        userDto.setEmail("asd@asd.ru");

        userService.createUser(userDto);
        itemService.createItem(itemDto,userId);
    }

    @Test
    void getAllItemsWithSearch() {
    }

    @Test
    void getItemBookings() {
    }

    @Test
    void addCommentByItemId() {
    }
}
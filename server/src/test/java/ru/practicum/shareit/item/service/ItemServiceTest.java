package ru.practicum.shareit.item.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.mappers.ItemMapper;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.mappers.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureMockMvc
class ItemServiceTest {

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemService itemService;

    @BeforeEach
    private void addUsers() {

        userService.createUser(UserDto.builder()
                .name("user1")
                .email("user1@user.ru")
                .build());

        userService.createUser(UserDto.builder()
                .name("user2")
                .email("user2@user.ru")
                .build());

        userService.createUser(UserDto.builder()
                .name("user3")
                .email("user3@user.ru")
                .build());

        itemService.createItem(ItemDto.builder()
                .name("item1")
                .available(true)
                .description("item 1 description")
                .build(), 1);
        itemService.createItem(ItemDto.builder()
                .name("item2")
                .description("test")
                .available(true)
                .description("item 1 description")
                .build(), 2);

        Booking booking = new Booking();
        booking.setBooker(userRepository.findById(2).get());
        booking.setItem(itemRepository.findById(1).get());
        booking.setStatus(Status.WAITING);
        booking.setStart(LocalDateTime.now().minusDays(1));
        booking.setEnd(LocalDateTime.now().minusSeconds(240));

        bookingRepository.save(booking);
    }


    @Transactional
    @Test
    @Sql(value = {"/delete_ALL.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAllItems() {

        bookingService.createBooking(BookingDto.builder()
                .booker(UserMapper.INSTANCE.userDtoToUser(userService.getUser(2)))
                .item(ItemMapper.INSTANCE.itemDtoToItem(itemService.getItemBookings(1, 1)))
                .itemId(1)
                .start(LocalDateTime.now().plusSeconds(120))
                .end(LocalDateTime.now().plusSeconds(240))
                .build(), 2);

        itemService.getAllItems(1);

        assertThat(userService.getUser(1), notNullValue());
        assertThat(itemService.getAllItems(1), notNullValue());

        assertEquals(itemService.getAllItems(1).size(), 1);
    }


    @Transactional
    @Test
    @Sql(value = {"/delete_ALL.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getItemBookings() {

        assertThat(userService.getUser(1), notNullValue());
        assertThat(itemService.getItemBookings(1, 2), notNullValue());
        Assertions.assertEquals(itemService.getItemBookings(1, 2).getId(), 1);
    }

    @Transactional
    @Test
    @Sql(value = {"/delete_ALL.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addCommentByItemId() {
        CommentDto commentDto = CommentDto.builder()
                .text("test")
                .build();

        Assertions.assertEquals(commentDto.getText(), itemService.addCommentByItemId(1, 2, commentDto).getText());
    }


    @Transactional
    @Test
    @Sql(value = {"/delete_ALL.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateItem() {
        int userId = 1;
        int itemId = 1;

        ItemDto expectedItemDto = ItemDto.builder()
                .id(1)
                .build();

        ItemDto actualItemDto = itemService.updateItem(expectedItemDto, itemId, userId);

        assertEquals(expectedItemDto.getId(), actualItemDto.getId());
    }


    @Transactional
    @Test
    @Sql(value = {"/delete_ALL.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAllItemsWithSearch() {
        Collection<ItemDto> itemDtos = itemService.getAllItemsWithSearch(0, "test");
        assertEquals(0, itemDtos.size());
    }
}
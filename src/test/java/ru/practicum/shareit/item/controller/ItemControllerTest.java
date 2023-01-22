package ru.practicum.shareit.item.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;


    @Test
    void createItem() {
        int userId = 0;
        ItemDto itemDto = ItemDto.builder().build();

        Mockito.when(itemService.createItem(itemDto, userId)).thenReturn(itemDto);
        ResponseEntity<ItemDto> responce = itemController.createItem(itemDto, userId);
        assertEquals(HttpStatus.OK, responce.getStatusCode());

    }

    @Test
    void updateItem() {
        int userId = 0;
        int itemId = 0;
        ItemDto itemDto = ItemDto.builder().build();

        Mockito.when(itemService.updateItem(itemDto, itemId, userId)).thenReturn(itemDto);
        ResponseEntity<ItemDto> responce = itemController.updateItem(itemId, itemDto, userId);
        assertEquals(HttpStatus.OK, responce.getStatusCode());
    }

    @Test
    void getItemById() {
        int userId = 0;
        int itemId = 0;

        ResponseEntity<ItemDto> responce = itemController.getItemById(itemId, userId);
        assertEquals(HttpStatus.OK, responce.getStatusCode());
    }

    @Test
    void addCommentByItemId() {
        int userId = 0;
        int itemId = 0;
        CommentDto commentDto = CommentDto.builder().build();


        ResponseEntity<CommentDto> responce = itemController.addCommentByItemId(itemId, userId, commentDto);
        assertEquals(HttpStatus.OK, responce.getStatusCode());
    }

    @Test
    void getAllItems() {
        int userId = 0;

        ResponseEntity<Collection<ItemDto>> responce = itemController.getAllItems(userId);
        assertEquals(HttpStatus.OK, responce.getStatusCode());
    }

    @Test
    void getAllItemsWithSearch() {
        int userId = 0;

        ResponseEntity<Collection<ItemDto>> responce = itemController.getAllItemsWithSearch("///", userId);
        assertEquals(HttpStatus.OK, responce.getStatusCode());
    }
}
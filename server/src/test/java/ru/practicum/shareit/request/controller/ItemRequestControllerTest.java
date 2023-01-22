package ru.practicum.shareit.request.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {

    @Mock
    private ItemRequestService itemRequestService;
    @InjectMocks
    private ItemRequestController itemRequestController;

    @Test
    void createItemRequest() {
        int userId = 0;
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        Mockito.when(itemRequestService.createItemRequest(itemRequestDto, userId)).thenReturn(itemRequestDto);
        ResponseEntity<ItemRequestDto> responce = itemRequestController.createItemRequest(itemRequestDto, userId);
        assertEquals(HttpStatus.OK, responce.getStatusCode());
    }

    @Test
    void getItemRequestDtoById() {
        int userId = 0;
        int itemId = 0;
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        Mockito.when(itemRequestService.getItemRequestById(itemId, userId)).thenReturn(itemRequestDto);
        ResponseEntity<ItemRequestDto> responce = itemRequestController.getItemRequestById(itemId, userId);
        assertEquals(HttpStatus.OK, responce.getStatusCode());
    }

    @Test
    void getItemRequests() {
        int itemId = 0;
        int userId = 0;
        List<ItemRequestDto> itemRequestDtoList = new ArrayList<>();
        Mockito.when(itemRequestService.getItemRequests(userId, 0, 10)).thenReturn(itemRequestDtoList);
        ResponseEntity<Collection<ItemRequestDto>> responce = itemRequestController.getItemRequests(itemId, 0, 10);
        assertEquals(HttpStatus.OK, responce.getStatusCode());
    }

    @Test
    void getItemRequestsAllOther() {
        int userId = 0;
        int itemId = 0;
        List<ItemRequestDto> itemRequestDtoList = new ArrayList<>();
        Mockito.when(itemRequestService.getItemRequestsAllOther(userId, 0, 10)).thenReturn(itemRequestDtoList);
        ResponseEntity<List<ItemRequestDto>> responce = itemRequestController.getItemRequestsAllOther(userId, 0, 10);
        assertEquals(HttpStatus.OK, responce.getStatusCode());
    }
}
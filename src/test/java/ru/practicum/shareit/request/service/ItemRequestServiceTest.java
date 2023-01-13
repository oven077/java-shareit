package ru.practicum.shareit.request.service;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.mappers.ItemMapper;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.mappers.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemRequestService itemRequestService;


    @Test
    void createItemRequest() {
        int userId = 0;
        User user = new User();
        ItemRequest itemRequestToSave = new ItemRequest();
        Mockito.when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(itemRequestToSave);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        ItemRequest actualItemRequest = ItemRequestMapper.INSTANCE.itemRequestDtoToItemRequest(itemRequestService
                .createItemRequest(ItemRequestMapper.INSTANCE.itemRequestToItemRequestDto(itemRequestToSave), userId));
        Assert.assertEquals(itemRequestToSave, actualItemRequest);
        Mockito.verify(itemRequestRepository).save(any(ItemRequest.class));
    }

    @Test
    void getItemRequests() {
        int userId = 0;
        User user = new User();
        ItemRequestDto itemRequestDtoToSave = new ItemRequestDto();

        List<ItemRequestDto> itemRequestDtoListExpected = new ArrayList<>();
        List<ItemDto> itemListDto = new ArrayList<>();
        itemRequestDtoToSave.setItems(itemListDto);


        itemRequestDtoListExpected.add(itemRequestDtoToSave);

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        final Pageable pageable = PageRequest.of(0, 10);

        Mockito.when(itemRepository.findAll()).thenReturn(ItemMapper.INSTANCE.targetListToSourceList(itemListDto));
        Mockito.when(itemRequestRepository.findAll()).thenReturn(ItemRequestMapper.INSTANCE
                .targetListToSourceList(itemRequestDtoListExpected));
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        List<ItemRequestDto> actualItemRequestListDto = itemRequestService.getItemRequests(userId, 0, 10);

        Assert.assertEquals(itemRequestDtoListExpected, actualItemRequestListDto);
        Mockito.verify(itemRequestRepository).findAll();
    }

    @Test
    void getItemRequestsAllOther() {

        int userId = 0;
        int itemId = 0;
        User user = new User();
        ItemRequestDto itemRequestDtoToSave = new ItemRequestDto();

        List<ItemRequestDto> itemRequestDtoListExpected = new ArrayList<>();
        List<ItemDto> itemListDto = new ArrayList<>();


        itemRequestDtoListExpected.add(itemRequestDtoToSave);

        Sort sort = Sort.by(Sort.Direction.DESC, "created");
        final Pageable pageable = PageRequest.of(0, 10, sort);

        Mockito.when(itemRepository.findAll()).thenReturn(ItemMapper.INSTANCE.targetListToSourceList(itemListDto));
        Mockito.when(itemRequestRepository.findAllOtherItemRequests(itemId, pageable)).thenReturn(ItemRequestMapper
                .INSTANCE.targetListToSourceList(itemRequestDtoListExpected));
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        List<ItemRequestDto> actualItemRequestListDto = itemRequestService.getItemRequestsAllOther(userId, 0, 10);

        Assert.assertEquals(itemRequestDtoListExpected.size(), actualItemRequestListDto.size());
        Mockito.verify(itemRequestRepository).findAllOtherItemRequests(itemId, pageable);

    }

    @Test
    void getItemRequestById() {

        int userId = 0;
        int itemId = 0;
        User user = new User();
        ItemRequest itemRequestToSave = new ItemRequest();

        Mockito.when(itemRequestRepository.findById(itemId)).thenReturn(Optional.of(itemRequestToSave));
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        ItemRequestDto actualItemRequestDto = itemRequestService.getItemRequestById(itemId, userId);

        Assert.assertEquals(itemRequestToSave, ItemRequestMapper.INSTANCE.itemRequestDtoToItemRequest(actualItemRequestDto));
        Mockito.verify(itemRequestRepository, Mockito.times(2)).findById(itemId);

    }
}
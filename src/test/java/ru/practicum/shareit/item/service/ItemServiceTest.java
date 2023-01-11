package ru.practicum.shareit.item.service;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.mappers.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;


    @Test
    void createItem() {
        int userId = 0;
        int itemRequestId = 0;
        Item item = new Item();

        User user = new User();
        ItemDto expectedItemDto = new ItemDto();

        ItemRequest itemRequest = new ItemRequest();

        Mockito.when(itemRepository.save(any(Item.class))).thenReturn(item);
        Mockito.when(itemRequestRepository.findById(itemRequestId)).thenReturn(Optional.of(itemRequest));
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        ItemDto actualItemDto = itemService.createItem(expectedItemDto, userId);


        Assert.assertEquals(expectedItemDto, actualItemDto);
        Mockito.verify(itemRequestRepository, Mockito.times(2)).findById(itemRequestId);
    }

    @Test
    void updateItem() {
        int userId = 0;
        int itemId = 0;
        Item item = new Item();

        ItemDto expectedItemDto = new ItemDto();

        Mockito.when(itemRepository.findByItemAndOwnerId(itemId, userId)).thenReturn(Optional.of(item));
        Mockito.when(itemRepository.save(any(Item.class))).thenReturn(item);
        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        ItemDto actualItemDto = itemService.updateItem(expectedItemDto, itemId, userId);


        Assert.assertEquals(expectedItemDto, actualItemDto);
    }

    @Test
    @Ignore
    void getAllItems() {

        int userId = 0;
        int itemId = 0;
        Item item = new Item();
        ItemDto itemDto = new ItemDto();

        List<Booking> listBooking = new ArrayList<>();


        Collection<ItemDto> expectedListItemDto = new ArrayList<>();
        List<ItemDto> itemDtoList = new ArrayList<>();

        itemDtoList.add(itemDto);


        Mockito.when(ItemMapper.INSTANCE
                .sourceListToTargetList(itemRepository.findAll().stream()
                        .filter(p -> p.getOwner().getId() == userId)
                        .sorted(Comparator.comparingInt(Item::getId))
                        .collect(Collectors.toList()))).thenReturn(any());

        Mockito.when(itemRepository.findListBooking(itemId, userId)).thenReturn(listBooking);
//        Mockito.when(itemRepository.findByItemAndOwnerId(itemId, userId)).thenReturn(Optional.of(item));
//        Mockito.when(itemRepository.save(any(Item.class))).thenReturn(item);
//        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        Collection<ItemDto> actualListItemDto = itemService.getAllItems(userId);


        Assert.assertEquals(expectedListItemDto.size(), actualListItemDto.size());
    }

    @Test
    @Ignore
    void getAllItemsWithSearch() {
    }

    @Test
    @Ignore
    void getItemBookings() {
    }

    @Test
    @Ignore
    void addCommentByItemId() {
    }
}
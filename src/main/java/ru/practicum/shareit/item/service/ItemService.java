package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NoSuchUserException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.mappers.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemService(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }


    public ItemDto createItem(ItemDto item, int userId) {
        Item newItem;
        if (userRepository.findById(userId).isPresent()) {
            newItem = ItemMapper.INSTANCE.itemDtoToItem(item);
            newItem.setOwner(userRepository.findById(userId).get());
            return ItemMapper.INSTANCE.itemToItemDto(itemRepository.save(newItem));
        } else {
            throw new NotFoundException("No User with this ID: " + userId);
        }
    }

    public ItemDto updateItem(ItemDto item, int itemId, int userId) {

        if (itemRepository.findByItemAndOwnerId(itemId, userId).isPresent()) {
            return ItemMapper.INSTANCE.itemToItemDto(itemRepository.save(ItemMapper.INSTANCE
                    .updateUserFromDto(item, itemRepository.findById(itemId).get())));
        } else {
            throw new NotFoundException("No Item with this ID or wrong ID owner: " + userId);
        }
    }

    public ItemDto getItem(int itemId) {
        if (itemRepository.findById(itemId).isPresent()) {
            return ItemMapper.INSTANCE.itemToItemDto(itemRepository.findById(itemId).get());
        } else {
            throw new NoSuchUserException("No Item with such ID: " + itemId);
        }
    }

    public Collection<ItemDto> getAllItems(int userId) {
        return ItemMapper.INSTANCE
                .sourceListToTargetList(itemRepository.findAll().stream()
                        .filter(p -> p.getOwner().getId() == userId)
                        .collect(Collectors.toList()));
    }


    public Collection<ItemDto> getAllItemsWithSearch(int userId, String text) {

        return ItemMapper.INSTANCE
                .sourceListToTargetList(itemRepository.findAll().stream()
                        .filter(p -> p.getDescription().toLowerCase().contains(text.toLowerCase()) && p.getAvailable().equals(true))
                        .collect(Collectors.toList()));
    }

}

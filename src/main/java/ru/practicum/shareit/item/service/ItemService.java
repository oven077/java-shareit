package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NoSuchUserException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.mappers.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDao;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class ItemService {

    private final UserDao userDao;
    private final ItemDao itemDao;
    int id = 1;

    public ItemService(UserDao userDao, ItemDao itemDao) {
        this.userDao = userDao;
        this.itemDao = itemDao;
    }


    public ItemDto createItem(ItemDto item, int userId) {
        Item newItem;
        if (userDao.findUserById(userId).isPresent()) {
            newItem = ItemMapper.INSTANCE.itemDtoToItem(item);
            newItem.setOwner(userId);
            newItem.setId(generateId());
            return ItemMapper.INSTANCE.itemToItemDto(itemDao.create(newItem));
        } else {
            throw new NotFoundException("No User with this ID: " + userId);
        }
    }

    public ItemDto updateItem(ItemDto item, int itemId, int userId) {

        if (itemDao.findItemByIdWithUser(itemId, userId).isPresent()) {
            return ItemMapper.INSTANCE.itemToItemDto(itemDao.update(ItemMapper.INSTANCE
                    .updateUserFromDto(item, itemDao.findItemByIdWithUser(itemId, userId).get()), userId));
        } else {
            throw new NotFoundException("No Item with this ID or wrong ID owner: " + userId);
        }
    }

    public ItemDto getItem(int itemId) {
        if (itemDao.readOne(itemId) != null) {
            return ItemMapper.INSTANCE.itemToItemDto(itemDao.readOne(itemId));
        } else {
            throw new NoSuchUserException("No Item with such ID: " + id);
        }
    }

    public Collection<ItemDto> getAllItems(int userId) {
        return ItemMapper.INSTANCE
                .sourceListToTargetList(itemDao.readAll().stream()
                        .filter(p -> p.getOwner() == userId)
                        .collect(Collectors.toList()));
    }

    public Collection<ItemDto> getAllItemsWithSearch(int userId, String text) {

        return ItemMapper.INSTANCE
                .sourceListToTargetList(itemDao.readAll().stream()
                        .filter(p -> p.getDescription().toLowerCase().contains(text.toLowerCase()) && p.getAvailable().equals(true))
                        .collect(Collectors.toList()));
    }


    public int generateId() {
        return id++;
    }

}

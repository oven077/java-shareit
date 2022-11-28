package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.dao.Dao;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;


public interface ItemDao extends Dao<Item> {

    Optional<Item> findItemByIdWithUser(int itemId, int userId);

    Optional<Item> findItemById(int itemId);

    List<Item> findAllItems();
}

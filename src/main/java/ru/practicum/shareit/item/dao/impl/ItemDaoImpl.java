package ru.practicum.shareit.item.dao.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ItemDaoImpl implements ItemDao {


    @Override
    public Item create(Item item) {

        items.put(item.getId(), item);

        return item;
    }

    @Override
    public Collection<Item> readAll() {
        return findAllItems();
    }

    @Override
    public Item readOne(int id) {

        if (findItemById(id).isPresent()) {
            return findItemById(id).get();
        } else {
            return null;
        }
    }

    @Override
    public Optional<Item> findItemByIdWithUser(int itemId, int userId) {

        return items.values().stream()
                .filter(p -> p.getId() == itemId && p.getOwner() == userId)
                .findFirst();
    }

    @Override
    public Optional<Item> findItemById(int itemId) {
        return items.values().stream()
                .filter(p -> p.getId() == itemId)
                .findFirst();
    }

    @Override
    public List<Item> findAllItems() {
        return items.values().stream()
                .collect(Collectors.toList());
    }


    @Override
    public Item update(Item item, int userId) {

        item.setOwner(userId);
        items.put(item.getId(), item);

        return item;
    }

    @Override
    public void delete(int id) {

    }


}

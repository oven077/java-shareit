package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    @Override
    @Query(value = "select * from items as i where i.id = ?", nativeQuery = true)
    Optional<Item> findById(@Param("id") Integer integer);


    @Query(value = "select * from items as i where i.id = ? and i.owner_id = ?", nativeQuery = true)
    Optional<Item> findByItemAndOwnerId(@Param("id") Integer itemId, @Param("owner_id") Integer ownerId);



}

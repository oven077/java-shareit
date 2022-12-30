package ru.practicum.shareit.dao;

import java.util.Collection;

public interface Dao<T> {

    T create(T param);

    Collection<T> readAll();

    T readOne(int id);

    T update(T param, int id);

    void delete(int id);


}

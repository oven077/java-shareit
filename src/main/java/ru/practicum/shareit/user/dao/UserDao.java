package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.dao.Dao;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;


public interface UserDao extends Dao<User> {
    HashMap<Integer, User> users = new HashMap<>();

    Optional<User> findUserById(int id);

    List<User> findAllUsers();

}

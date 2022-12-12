package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.dao.Dao;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;


public interface UserDao extends Dao<User> {

    Optional<User> findUserById(int id);

    List<User> findAllUsers();

}

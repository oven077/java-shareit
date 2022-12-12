package ru.practicum.shareit.user.dao.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserDaoImpl implements UserDao {

    private final HashMap<Integer, User> users = new HashMap<>();

    @Override
    public User create(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Collection<User> readAll() {
        return findAllUsers();
    }


    @Override
    public User readOne(int id) {

        if (findUserById(id).isPresent()) {
            return findUserById(id).get();
        } else {
            return null;
        }
    }

    @Override
    public User update(User user, int id) {

        users.put(id, user);
        return user;
    }

    @Override
    public void delete(int id) {
        users.remove(id);
    }


    @Override
    public Optional<User> findUserById(int id) {

        return users.values().stream()
                .filter(p -> p.getId() == id)
                .findFirst();
    }

    @Override
    public List<User> findAllUsers() {

        return users.values().stream()
                .collect(Collectors.toList());

    }
}

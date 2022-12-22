package ru.practicum.shareit.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

@Component

public interface UserRepository extends JpaRepository<User, Integer> {

    @Override
    @Query(value = "select * from users as u where u.id = ?", nativeQuery = true)
    Optional<User> findById(@Param("id") Integer id);


}
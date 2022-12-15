package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.io.Serializable;

/**
 * TODO Sprint add-controllers.
 */


@Entity
@Data
@Table(schema = "public", name = "items")
public class Item implements Serializable {
    @Id
    @Column(name = "id")
    public int id;
    public String name;
    public String description;
    @Column(name = "is_available")
    public Boolean available;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    public User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    public ItemRequest request;

}

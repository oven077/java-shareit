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
    @SequenceGenerator(name = "pk_sequence", schema = "public", sequenceName = "pk_sequence_1", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence_1")
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

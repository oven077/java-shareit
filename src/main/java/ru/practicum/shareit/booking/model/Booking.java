package ru.practicum.shareit.booking.model;

import lombok.Data;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Entity
@Data
@Table(schema = "public", name = "bookings")
public class Booking {

    @Id
    @Column(name = "id")
    public int id;
    @Column(name = "start_date")
    public LocalDateTime start;
    @Column(name = "end_date")
    public LocalDateTime end;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    public Item item;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booker_id")
    public User booker;
    @Enumerated(EnumType.STRING)
    public Status status;

}

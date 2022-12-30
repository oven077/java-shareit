package ru.practicum.shareit.request.model;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

/**
 * TODO Sprint add-item-requests.
 */

@Entity
@Data
@Table(schema = "public", name = "requests")
public class ItemRequest {

    @Id
    @Column(name = "id")
    public int id;
    public String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestor_id")
    public User requestor;
}

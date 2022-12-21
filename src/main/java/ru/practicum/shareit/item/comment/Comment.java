package ru.practicum.shareit.item.comment;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Entity
@Data
@Table(schema = "public", name = "comments")

public class Comment {

    @Id
    @Column(name = "id")
    public int id;
    public String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    public Item item;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    public User author;


}

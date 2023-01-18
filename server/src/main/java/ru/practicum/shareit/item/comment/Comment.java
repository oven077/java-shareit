package ru.practicum.shareit.item.comment;

import lombok.Data;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.item.model.Item;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(schema = "public", name = "comments")

public class Comment {

    @Id
    @SequenceGenerator(name = "pk_sequence", schema = "public", sequenceName = "comments_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id")
    private int id;
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;
    private LocalDateTime created;


}

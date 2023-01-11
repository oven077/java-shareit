package ru.practicum.shareit.request.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */

@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(schema = "public", name = "requests")
public class ItemRequest {

    @Id
    @SequenceGenerator(name = "pk_sequence", schema = "public", sequenceName = "requests_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id")
    public int id;
    public String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestor_id")
    public User requestor;
    private LocalDateTime created;

}

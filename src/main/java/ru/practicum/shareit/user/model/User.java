package ru.practicum.shareit.user.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * TODO Sprint add-controllers.
 */

@Entity
@Data
@Table(schema = "public", name = "users")

public class User implements Serializable {

    @Id
    @Column(name = "id")
    private int id;
    private String name;
    private String email;
}

package ru.practicum.shareit.user.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;

/**
 * TODO Sprint add-controllers.
 */

@Entity
@Data
@Table(schema = "public", name = "users", uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))

public class User implements Serializable {

    @Id
    @SequenceGenerator(name = "pk_sequence", schema = "public", sequenceName = "pk_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence")
    @Column(name = "id")
    private int id;
    private String name;
    @Column(name = "email")
    @Email
    private String email;
}

package ru.practicum.shareit.item.model;

import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */


@Data
public class Item {

    public int id;
    public String name;
    public String description;
    public Boolean available;
    public int owner;
    public String request;

}

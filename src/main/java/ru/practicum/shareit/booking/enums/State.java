package ru.practicum.shareit.booking.enums;

import java.util.Arrays;
import java.util.Objects;

public enum State {

    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED,
    UNKNOWN;

    public static State of(String value) {
        return Arrays.stream(values())
                .filter(item -> Objects.equals(value, item.name()))
                .findFirst()
                .orElse(UNKNOWN);
    }
}

package ru.practicum.shareit.exceptions;

import java.util.NoSuchElementException;

public class NoSuchUserException extends NoSuchElementException {
    public NoSuchUserException(String message) {
        super(message);
    }
}

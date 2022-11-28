package ru.practicum.shareit.error;

public class AppError {
    private final int statusCode;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public AppError(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}

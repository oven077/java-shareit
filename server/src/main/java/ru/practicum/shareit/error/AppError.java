package ru.practicum.shareit.error;

public class AppError {
    private final int statusCode;
    private String error;

    public AppError(int statusCode, String message) {
        this.statusCode = statusCode;
        this.error = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

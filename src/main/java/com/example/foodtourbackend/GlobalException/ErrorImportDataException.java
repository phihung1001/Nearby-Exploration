package com.example.foodtourbackend.GlobalException;

public class ErrorImportDataException extends RuntimeException {
    public ErrorImportDataException(String message) {
        super(message);
    }
}

package com.example.back.exceptions;

public class UnauthoziedExcepetion extends RuntimeException {
    public UnauthoziedExcepetion(String message) {
        super(message);
    }
}

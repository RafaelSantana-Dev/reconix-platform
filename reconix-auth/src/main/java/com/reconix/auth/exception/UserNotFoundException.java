package com.reconix.auth.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String userId) {
        super("Usuario nao encontrado: " + userId);
    }
}
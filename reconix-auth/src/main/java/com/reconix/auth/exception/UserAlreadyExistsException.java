package com.reconix.auth.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String username) {
        super("Usuario ja existe: " + username);
    }
}
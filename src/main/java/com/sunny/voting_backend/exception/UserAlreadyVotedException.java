package com.sunny.voting_backend.exception;

public class UserAlreadyVotedException extends RuntimeException{
    public UserAlreadyVotedException(String message) {
        super(message);
    }

    public UserAlreadyVotedException(String message, Throwable cause) {
        super(message, cause);
    }
}

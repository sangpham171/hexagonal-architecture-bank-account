package com.lightdevops.bankaccount.adapters.rest.exceptions;

public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}

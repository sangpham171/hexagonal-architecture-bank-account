package com.lightdevops.bankaccount.domain.exceptions;


public class TransactionException extends RuntimeException {
    public TransactionException(String message) {
        super(message);
    }
}

package com.lightdevops.bankaccount.domain.exceptions;


public class BankAccountException extends RuntimeException {
    public BankAccountException(String message) {
        super(message);
    }
}

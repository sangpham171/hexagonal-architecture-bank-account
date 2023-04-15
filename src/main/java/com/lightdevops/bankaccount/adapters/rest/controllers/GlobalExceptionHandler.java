package com.lightdevops.bankaccount.adapters.rest.controllers;

import com.lightdevops.bankaccount.adapters.rest.exceptions.AuthException;
import com.lightdevops.bankaccount.domain.exceptions.BankAccountException;
import com.lightdevops.bankaccount.domain.exceptions.BankAccountExceptionMessage;
import com.lightdevops.bankaccount.domain.exceptions.TransactionException;
import com.lightdevops.bankaccount.domain.exceptions.TransactionExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<String> handleAuthException(AuthException e) {
        return switch (e.getMessage()) {
            case BankAccountExceptionMessage.FORBIDDEN_SEARCH_BALANCE,
                    TransactionExceptionMessage.FORBIDDEN_DEPOSIT,
                    TransactionExceptionMessage.FORBIDDEN_SEARCH_TRANSACTIONS,
                    TransactionExceptionMessage.FORBIDDEN_WITHDRAW ->
                    ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        };
    }

    @ExceptionHandler(BankAccountException.class)
    public ResponseEntity<String> handleBankAccountException(BankAccountException e) {
        return switch (e.getMessage()) {
            case BankAccountExceptionMessage.ACCOUNT_NOT_FOUND ->
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            case BankAccountExceptionMessage.INVALID_ACCOUNT_ID,
                    BankAccountExceptionMessage.EXCEED_WITHDRAW_LIMIT_PER_WEEK,
                    BankAccountExceptionMessage.INSUFFICIENT_BALANCE ->
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        };
    }

    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<String> handleBankAccountException(TransactionException e) {
        return switch (e.getMessage()) {
            case TransactionExceptionMessage.INVALID_AMOUNT,
                    TransactionExceptionMessage.INVALID_TRANSACTION_INPUT ->
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        };
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
    }
}

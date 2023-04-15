package com.lightdevops.bankaccount.domain.exceptions;

public class TransactionExceptionMessage {
    public static final String FORBIDDEN_DEPOSIT = "Forbidden deposit";
    public static final String FORBIDDEN_SEARCH_TRANSACTIONS = "Forbidden search transactions";
    public static final String FORBIDDEN_WITHDRAW = "Forbidden withdraw";
    public static final String INVALID_AMOUNT = "Invalid amount: must be greater than zero";
    public static final String INVALID_TRANSACTION_INPUT = "Invalid transaction input";
}

package com.lightdevops.bankaccount.adapters.rest.services;

import com.lightdevops.bankaccount.adapters.rest.dtos.TransactionReq;
import com.lightdevops.bankaccount.adapters.rest.dtos.TransactionRes;

import java.util.List;

public interface ITransferService {
    TransactionRes deposit(TransactionReq transactionReq);

    List<TransactionRes> findTransactionsByBankCountId(Long bankAccountId);

    TransactionRes withdraw(TransactionReq transactionReq);
}

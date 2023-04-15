package com.lightdevops.bankaccount.domain.ports.outbound;

import com.lightdevops.bankaccount.domain.model.Transaction;

import java.util.List;

public interface IFindTransactionPort {
    List<Transaction> findTransactionByBankAccountId(Long bankAccountId);
}

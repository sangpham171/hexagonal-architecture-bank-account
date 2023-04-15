package com.lightdevops.bankaccount.domain.ports.inbound;

import com.lightdevops.bankaccount.domain.dto.TransactionOutputDto;

import java.util.List;

public interface ISearchTransactionUseCase {
    List<TransactionOutputDto> searchTransactionByBankAccountId(Long bankAccountId);
}

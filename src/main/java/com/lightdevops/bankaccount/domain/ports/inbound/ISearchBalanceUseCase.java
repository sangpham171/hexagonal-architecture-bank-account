package com.lightdevops.bankaccount.domain.ports.inbound;

public interface ISearchBalanceUseCase {
    Double searchAvailableBalanceByBankAccountId(Long bankAccountId);
}

package com.lightdevops.bankaccount;

import com.lightdevops.bankaccount.adapters.persistence.entities.BankAccountEntity;
import com.lightdevops.bankaccount.adapters.persistence.entities.TransactionEntity;
import com.lightdevops.bankaccount.adapters.rest.dtos.TransactionReq;
import com.lightdevops.bankaccount.domain.dto.TransactionInputDto;
import com.lightdevops.bankaccount.domain.model.BankAccount;
import com.lightdevops.bankaccount.domain.model.Transaction;
import com.lightdevops.bankaccount.domain.model.enums.TransactionStatus;
import com.lightdevops.bankaccount.domain.model.enums.TransactionType;

import java.time.LocalDateTime;
import java.util.List;

public class FakeAccountData {
    public Long id = 1L;
    public Double availableBalance = 300.0;
    public Double pendingBalance = 0.0;
    public Double eligibleBalance = availableBalance + pendingBalance;
    public Integer withdrawLimitPerWeek = 1000;
    public Integer currentWithdrawPerWeek = 900;
    public Double transactionAmount = 10.0;

    public BankAccount bankAccount = BankAccount.builder()
                                                .id(id)
                                                .availableBalance(availableBalance)
                                                .pendingBalance(pendingBalance)
                                                .withdrawLimitPerWeek(withdrawLimitPerWeek)
                                                .currentWithdrawPerWeek(currentWithdrawPerWeek)
                                                .build();

    public BankAccountEntity bankAccountEntity = BankAccountEntity.builder()
                                                                  .id(id)
                                                                  .availableBalance(availableBalance)
                                                                  .pendingBalance(pendingBalance)
                                                                  .withdrawLimitPerWeek(withdrawLimitPerWeek)
                                                                  .currentWithdrawPerWeek(currentWithdrawPerWeek)
                                                                  .build();

    public List<Transaction> transactions =
            List.of(Transaction.builder()
                               .id(1L)
                               .transactionType(TransactionType.DEPOSIT)
                               .bankAccountId(id)
                               .amount(100.0)
                               .build());

    public TransactionInputDto transactionInputDto = TransactionInputDto.builder()
                                                                        .amount(transactionAmount)
                                                                        .bankAccountId(id)
                                                                        .currency("EUR")
                                                                        .build();

    public TransactionReq transactionReq = TransactionReq.builder()
                                                         .amount(transactionAmount)
                                                         .bankAccountId(id)
                                                         .currency("EUR")
                                                         .build();

    public List<TransactionEntity> transactionEntities =
            List.of(TransactionEntity.builder()
                                     .id(1L)
                                     .amount(100.0)
                                     .currency("EUR")
                                     .description("Deposit")
                                     .bankAccountId(id)
                                     .transactionType(TransactionType.DEPOSIT)
                                     .transactionStatus(TransactionStatus.SUCCESS)
                                     .createdDate(LocalDateTime.parse("2023-01-01T00:00:01"))
                                     .build());

}

package com.lightdevops.bankaccount.adapters.persistence.mappers;

import com.lightdevops.bankaccount.adapters.persistence.entities.BankAccountEntity;
import com.lightdevops.bankaccount.adapters.persistence.entities.TransactionEntity;
import com.lightdevops.bankaccount.domain.model.BankAccount;
import com.lightdevops.bankaccount.domain.model.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IMapperEntity {
    BankAccountEntity domainToEntity(BankAccount bankAccount);

    TransactionEntity domainToEntity(Transaction transaction);

    BankAccount entityToDomain(BankAccountEntity bankAccountEntity);

    Transaction entityToDomain(TransactionEntity transactionEntity);
}

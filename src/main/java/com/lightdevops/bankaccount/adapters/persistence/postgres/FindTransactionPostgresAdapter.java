package com.lightdevops.bankaccount.adapters.persistence.postgres;

import com.lightdevops.bankaccount.adapters.persistence.entities.TransactionEntity;
import com.lightdevops.bankaccount.adapters.persistence.mappers.IMapperEntity;
import com.lightdevops.bankaccount.adapters.persistence.postgres.repositories.TransactionRepository;
import com.lightdevops.bankaccount.domain.model.Transaction;
import com.lightdevops.bankaccount.domain.ports.outbound.IFindTransactionPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FindTransactionPostgresAdapter implements IFindTransactionPort {

    private final TransactionRepository transactionRepository;
    private final IMapperEntity domainMapper;

    @Override
    public List<Transaction> findTransactionByBankAccountId(Long bankAccountId) {
        List<TransactionEntity> transactions = transactionRepository.findByBankAccountId(bankAccountId);
        return transactions.stream().map(domainMapper::entityToDomain).toList();
    }
}

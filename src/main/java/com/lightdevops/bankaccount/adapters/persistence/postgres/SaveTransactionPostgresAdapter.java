package com.lightdevops.bankaccount.adapters.persistence.postgres;

import com.lightdevops.bankaccount.adapters.persistence.entities.TransactionEntity;
import com.lightdevops.bankaccount.adapters.persistence.mappers.IMapperEntity;
import com.lightdevops.bankaccount.adapters.persistence.postgres.repositories.TransactionRepository;
import com.lightdevops.bankaccount.domain.model.Transaction;
import com.lightdevops.bankaccount.domain.ports.outbound.ISaveTransactionPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SaveTransactionPostgresAdapter implements ISaveTransactionPort {

    private final TransactionRepository transactionRepository;
    private final IMapperEntity domainMapper;

    @Override
    public Transaction save(Transaction transaction) {
        TransactionEntity transactionEntity = transactionRepository.saveAndFlush(domainMapper.domainToEntity(transaction));
        return domainMapper.entityToDomain(transactionEntity);
    }
}

package com.lightdevops.bankaccount.adapters.persistence.postgres;

import com.lightdevops.bankaccount.adapters.persistence.entities.BankAccountEntity;
import com.lightdevops.bankaccount.adapters.persistence.mappers.IMapperEntity;
import com.lightdevops.bankaccount.adapters.persistence.postgres.repositories.BankAccountRepository;
import com.lightdevops.bankaccount.domain.model.BankAccount;
import com.lightdevops.bankaccount.domain.ports.outbound.IFindBankAccountPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FindBankAccountPostgresAdapter implements IFindBankAccountPort {

    private final BankAccountRepository bankAccountRepository;
    private final IMapperEntity domainMapper;

    @Override
    public Optional<BankAccount> findBankAccountById(Long id) {
        Optional<BankAccountEntity> accountEntity = bankAccountRepository.findById(id);
        return accountEntity.map(domainMapper::entityToDomain);
    }
}

package com.lightdevops.bankaccount.adapters.persistence.postgres;

import com.lightdevops.bankaccount.adapters.persistence.entities.BankAccountEntity;
import com.lightdevops.bankaccount.adapters.persistence.mappers.IMapperEntity;
import com.lightdevops.bankaccount.adapters.persistence.postgres.repositories.BankAccountRepository;
import com.lightdevops.bankaccount.domain.model.BankAccount;
import com.lightdevops.bankaccount.domain.ports.outbound.ISaveBankAccountPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SaveBankAccountPostgresAdapter implements ISaveBankAccountPort {

    private final BankAccountRepository bankAccountRepository;
    private final IMapperEntity domainMapper;

    @Override
    public void save(BankAccount bankAccount) {
        BankAccountEntity bankAccountEntity = domainMapper.domainToEntity(bankAccount);
        bankAccountRepository.save(bankAccountEntity);
    }
}

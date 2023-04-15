package com.lightdevops.bankaccount.adapters.persistence.postgres.repositories;

import com.lightdevops.bankaccount.adapters.persistence.entities.BankAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Long> {
}

package com.lightdevops.bankaccount.adapters.persistence.postgres.repositories;

import com.lightdevops.bankaccount.adapters.persistence.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findByBankAccountId(Long bankAccountId);
}

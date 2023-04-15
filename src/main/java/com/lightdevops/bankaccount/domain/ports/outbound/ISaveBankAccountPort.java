package com.lightdevops.bankaccount.domain.ports.outbound;

import com.lightdevops.bankaccount.domain.model.BankAccount;

import java.util.Optional;

public interface ISaveBankAccountPort {
    void save(BankAccount bankAccount);
}

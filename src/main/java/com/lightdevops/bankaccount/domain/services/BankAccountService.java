package com.lightdevops.bankaccount.domain.services;

import com.lightdevops.bankaccount.domain.exceptions.BankAccountException;
import com.lightdevops.bankaccount.domain.exceptions.BankAccountExceptionMessage;
import com.lightdevops.bankaccount.domain.model.BankAccount;
import com.lightdevops.bankaccount.domain.ports.inbound.ISearchBalanceUseCase;
import com.lightdevops.bankaccount.domain.ports.outbound.IFindBankAccountPort;
import com.lightdevops.bankaccount.domain.ports.outbound.ISaveBankAccountPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BankAccountService implements ISearchBalanceUseCase {
    private final IFindBankAccountPort findBankAccountPort;
    private final ISaveBankAccountPort saveBankAccountPort;

    @Override
    public Double searchAvailableBalanceByBankAccountId(Long bankAccountId) {
        validateBankAccountId(bankAccountId);

        BankAccount bankAccount = findBankAccountById(bankAccountId);
        return bankAccount.getAvailableBalance();
    }

    protected BankAccount findBankAccountById(Long id) {
        return findBankAccountPort.findBankAccountById(id)
                                  .orElseThrow(() -> new BankAccountException(BankAccountExceptionMessage.ACCOUNT_NOT_FOUND));
    }

    protected boolean isBalanceSufficientForWithdraw(BankAccount bankAccount, Double amount) {
        Double availableBalance = bankAccount.getAvailableBalance();
        Double pendingBalance = bankAccount.getPendingBalance();
        return amount <= Math.min(availableBalance, availableBalance + pendingBalance);
    }

    protected boolean isExceedWithdrawLimitPerWeek(BankAccount bankAccount, Double amount) {
        Integer currentWithdrawPerWeek = bankAccount.getCurrentWithdrawPerWeek();
        Integer withdrawLimitPerWeek = bankAccount.getWithdrawLimitPerWeek();
        return currentWithdrawPerWeek >= withdrawLimitPerWeek ||
               currentWithdrawPerWeek + amount > withdrawLimitPerWeek;
    }

    protected void save(BankAccount bankAccount) {
        saveBankAccountPort.save(bankAccount);
    }

    protected void validateBankAccountId(Long bankAccountId) {
        if (bankAccountId == null || bankAccountId <= 0)
            throw new BankAccountException(BankAccountExceptionMessage.INVALID_ACCOUNT_ID);
    }
}

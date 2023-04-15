package com.lightdevops.bankaccount.domain.services;

import com.lightdevops.bankaccount.domain.dto.IMapperDto;
import com.lightdevops.bankaccount.domain.dto.TransactionInputDto;
import com.lightdevops.bankaccount.domain.dto.TransactionOutputDto;
import com.lightdevops.bankaccount.domain.exceptions.BankAccountException;
import com.lightdevops.bankaccount.domain.exceptions.BankAccountExceptionMessage;
import com.lightdevops.bankaccount.domain.exceptions.TransactionException;
import com.lightdevops.bankaccount.domain.exceptions.TransactionExceptionMessage;
import com.lightdevops.bankaccount.domain.model.BankAccount;
import com.lightdevops.bankaccount.domain.model.Transaction;
import com.lightdevops.bankaccount.domain.model.enums.TransactionStatus;
import com.lightdevops.bankaccount.domain.model.enums.TransactionType;
import com.lightdevops.bankaccount.domain.ports.inbound.IDepositUseCase;
import com.lightdevops.bankaccount.domain.ports.inbound.ISearchTransactionUseCase;
import com.lightdevops.bankaccount.domain.ports.inbound.IWithdrawUseCase;
import com.lightdevops.bankaccount.domain.ports.outbound.IFindTransactionPort;
import com.lightdevops.bankaccount.domain.ports.outbound.ISaveTransactionPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TransactionService implements IDepositUseCase, IWithdrawUseCase, ISearchTransactionUseCase {
    private final IFindTransactionPort findTransactionPort;
    private final ISaveTransactionPort saveTransactionPort;
    private final IMapperDto mapperDto;
    private final BankAccountService bankAccountService;

    @Override
    public TransactionOutputDto deposit(TransactionInputDto transactionInputDto) {
        validateInputDto(transactionInputDto);
        return transferAmount(transactionInputDto, TransactionType.DEPOSIT);
    }

    @Override
    public List<TransactionOutputDto> searchTransactionByBankAccountId(Long bankAccountId) {
        bankAccountService.validateBankAccountId(bankAccountId);

        List<Transaction> transactions = findTransactionPort.findTransactionByBankAccountId(bankAccountId);
        return transactions.stream().map(mapperDto::modelToOutputDto).toList();
    }

    @Override
    public TransactionOutputDto withdraw(TransactionInputDto transactionInputDto) {
        validateInputDto(transactionInputDto);
        return transferAmount(transactionInputDto, TransactionType.WITHDRAW);
    }

    private Transaction createTransaction(TransactionInputDto transactionInputDto, TransactionType transactionType) {
        Transaction transaction = mapperDto.inputDtoToModel(transactionInputDto);
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        transaction.setTransactionType(transactionType);
        return saveTransactionPort.save(transaction);
    }

    private TransactionOutputDto transferAmount(TransactionInputDto transactionInputDto, TransactionType transactionType) {
        Double amount = transactionInputDto.getAmount();

        BankAccount bankAccount = bankAccountService.findBankAccountById(transactionInputDto.getBankAccountId());

        if (transactionType == TransactionType.WITHDRAW) validateTransactionAmount(bankAccount, amount);

        Double transactionAmount = TransactionType.DEPOSIT.equals(transactionType) ? amount : -amount;
        updateAvailableBalance(bankAccount, transactionAmount);

        Transaction transaction = createTransaction(transactionInputDto, transactionType);
        return mapperDto.modelToOutputDto(transaction);
    }

    private void updateAvailableBalance(BankAccount bankAccount, Double amount) {
        Double newAvailableBalance = bankAccount.getAvailableBalance() + amount;
        bankAccount.setAvailableBalance(newAvailableBalance);
        bankAccountService.save(bankAccount);
    }

    private void validateInputDto(TransactionInputDto transactionInputDto) {
        if (transactionInputDto.getAmount() <= 0 || transactionInputDto.getBankAccountId() <= 0)
            throw new TransactionException(TransactionExceptionMessage.INVALID_TRANSACTION_INPUT);
    }

    private void validateTransactionAmount(BankAccount bankAccount, Double amount) {
        if (!bankAccountService.isBalanceSufficientForWithdraw(bankAccount, amount))
            throw new BankAccountException(BankAccountExceptionMessage.INSUFFICIENT_BALANCE);

        if (bankAccountService.isExceedWithdrawLimitPerWeek(bankAccount, amount))
            throw new BankAccountException(BankAccountExceptionMessage.EXCEED_WITHDRAW_LIMIT_PER_WEEK);
    }

}

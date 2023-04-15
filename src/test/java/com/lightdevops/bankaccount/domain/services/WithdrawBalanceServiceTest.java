package com.lightdevops.bankaccount.domain.services;

import com.lightdevops.bankaccount.FakeAccountData;
import com.lightdevops.bankaccount.domain.dto.IMapperDto;
import com.lightdevops.bankaccount.domain.dto.TransactionInputDto;
import com.lightdevops.bankaccount.domain.exceptions.BankAccountException;
import com.lightdevops.bankaccount.domain.exceptions.BankAccountExceptionMessage;
import com.lightdevops.bankaccount.domain.exceptions.TransactionException;
import com.lightdevops.bankaccount.domain.exceptions.TransactionExceptionMessage;
import com.lightdevops.bankaccount.domain.model.BankAccount;
import com.lightdevops.bankaccount.domain.ports.outbound.IFindBankAccountPort;
import com.lightdevops.bankaccount.domain.ports.outbound.IFindTransactionPort;
import com.lightdevops.bankaccount.domain.ports.outbound.ISaveBankAccountPort;
import com.lightdevops.bankaccount.domain.ports.outbound.ISaveTransactionPort;
import org.junit.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@SpringBootTest
public class WithdrawBalanceServiceTest {

    private final IFindBankAccountPort findBankAccountPort = mock(IFindBankAccountPort.class);
    private final ISaveBankAccountPort saveBankAccountPort = mock(ISaveBankAccountPort.class);
    private final IFindTransactionPort findTransactionPort = mock(IFindTransactionPort.class);
    private final ISaveTransactionPort saveTransactionPort = mock(ISaveTransactionPort.class);
    private final IMapperDto mapperDto = Mappers.getMapper(IMapperDto.class);
    private final BankAccountService bankAccountService = new BankAccountService(findBankAccountPort, saveBankAccountPort);
    private final TransactionService transactionService = new TransactionService(findTransactionPort, saveTransactionPort, mapperDto, bankAccountService);
    private final FakeAccountData fakeAccountData = new FakeAccountData();
    private final BankAccount bankAccount = fakeAccountData.bankAccount;
    private final Long id = fakeAccountData.id;
    private final Double availableBalance = fakeAccountData.availableBalance;
    private final Double transactionAmount = fakeAccountData.transactionAmount;
    private final Integer withdrawLimitPerWeek = fakeAccountData.withdrawLimitPerWeek;
    private final Integer currentWithdrawPerWeek = fakeAccountData.currentWithdrawPerWeek;
    private final TransactionInputDto transactionInputDto = fakeAccountData.transactionInputDto;

    @Test(expected = BankAccountException.class)
    public void shouldShowExceptionWhenWithdrawAccountNotFound() {
        //Given
        given(findBankAccountPort.findBankAccountById(id)).willReturn(Optional.empty());
        //When
        try {
            transactionService.withdraw(transactionInputDto);
        } catch (BankAccountException e) {
            //Then
            assert BankAccountExceptionMessage.ACCOUNT_NOT_FOUND.equals(e.getMessage());
            throw e;
        }
    }

    @Test(expected = BankAccountException.class)
    public void shouldShowExceptionWhenWithdrawMoreThanAvailable() {
        //Given
        given(findBankAccountPort.findBankAccountById(id)).willReturn(Optional.of(bankAccount));
        transactionInputDto.setAmount(availableBalance + 1.0);

        //When
        try {
            transactionService.withdraw(transactionInputDto);
        } catch (BankAccountException e) {
            //Then
            assert BankAccountExceptionMessage.INSUFFICIENT_BALANCE.equals(e.getMessage());
            throw e;
        }
    }

    @Test(expected = BankAccountException.class)
    public void shouldShowExceptionWhenWithdrawMoreThanLimitPerWeek() {
        //Given
        given(findBankAccountPort.findBankAccountById(id)).willReturn(Optional.of(bankAccount));
        transactionInputDto.setAmount((double) (withdrawLimitPerWeek - currentWithdrawPerWeek + 1));
        //When
        try {
            transactionService.withdraw(transactionInputDto);
        } catch (BankAccountException e) {
            //Then
            assert BankAccountExceptionMessage.EXCEED_WITHDRAW_LIMIT_PER_WEEK.equals(e.getMessage());
            throw e;
        }
    }

    @Test(expected = TransactionException.class)
    public void shouldShowExceptionWhenWithdrawNegativeAmount() {
        //Given
        given(findBankAccountPort.findBankAccountById(id)).willReturn(Optional.of(bankAccount));
        transactionInputDto.setAmount(-1.0);
        //When
        try {
            transactionService.withdraw(transactionInputDto);
        } catch (TransactionException e) {
            //Then
            assert TransactionExceptionMessage.INVALID_TRANSACTION_INPUT.equals(e.getMessage());
            throw e;
        }
    }

    @Test(expected = TransactionException.class)
    public void shouldShowExceptionWhenWithdrawZeroAmount() {
        //Given
        given(findBankAccountPort.findBankAccountById(id)).willReturn(Optional.of(bankAccount));
        transactionInputDto.setAmount(0.0);
        //When
        try {
            transactionService.withdraw(transactionInputDto);
        } catch (TransactionException e) {
            //Then
            assert TransactionExceptionMessage.INVALID_TRANSACTION_INPUT.equals(e.getMessage());
            throw e;
        }
    }

    @Test
    public void shouldWithdrawBalance() {
        //Given
        given(findBankAccountPort.findBankAccountById(id)).willReturn(Optional.of(bankAccount));
        //When
        transactionService.withdraw(transactionInputDto);
        //Then
        assert bankAccount.getAvailableBalance() == availableBalance - transactionAmount;
    }

}

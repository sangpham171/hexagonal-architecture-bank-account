package com.lightdevops.bankaccount.domain.services;

import com.lightdevops.bankaccount.FakeAccountData;
import com.lightdevops.bankaccount.domain.exceptions.BankAccountException;
import com.lightdevops.bankaccount.domain.exceptions.BankAccountExceptionMessage;
import com.lightdevops.bankaccount.domain.model.BankAccount;
import com.lightdevops.bankaccount.domain.ports.outbound.IFindBankAccountPort;
import com.lightdevops.bankaccount.domain.ports.outbound.ISaveBankAccountPort;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@SpringBootTest
public class BankAccountServiceTest {
    private final IFindBankAccountPort findBankAccountPort = mock(IFindBankAccountPort.class);
    private final ISaveBankAccountPort saveBankAccountPort = mock(ISaveBankAccountPort.class);
    private final BankAccountService bankAccountService = new BankAccountService(findBankAccountPort, saveBankAccountPort);
    private final FakeAccountData fakeAccountData = new FakeAccountData();
    private final BankAccount bankAccount = fakeAccountData.bankAccount;
    private final Long id = bankAccount.getId();
    private final Double availableBalance = fakeAccountData.availableBalance;

    @Test
    public void isBalanceSufficientForWithdrawTest() {
        Double amount = fakeAccountData.eligibleBalance - 1;
        boolean isBalanceSufficientForWithdraw = bankAccountService.isBalanceSufficientForWithdraw(fakeAccountData.bankAccount, amount);
        assert isBalanceSufficientForWithdraw;
    }

    @Test
    public void isExceedWithdrawLimitPerWeekTest() {
        double amount = fakeAccountData.withdrawLimitPerWeek - fakeAccountData.currentWithdrawPerWeek - 1;
        boolean isExceedWithdrawLimitPerWeek = bankAccountService.isExceedWithdrawLimitPerWeek(fakeAccountData.bankAccount, amount);
        assert !isExceedWithdrawLimitPerWeek;
    }

    @Test
    public void shouldShowBalance() {
        //Given
        given(findBankAccountPort.findBankAccountById(id)).willReturn(Optional.of(bankAccount));
        //When
        Double availableBalance = bankAccountService.searchAvailableBalanceByBankAccountId(id);
        //Then
        assert this.availableBalance.equals(availableBalance);
    }

    @Test(expected = BankAccountException.class)
    public void shouldShowExceptionWhenConsultBalanceAccountNotFound() {
        //Given
        given(findBankAccountPort.findBankAccountById(id)).willReturn(Optional.empty());
        //When
        try {
            bankAccountService.searchAvailableBalanceByBankAccountId(id);
        } catch (BankAccountException e) {
            //Then
            assert BankAccountExceptionMessage.ACCOUNT_NOT_FOUND.equals(e.getMessage());
            throw e;
        }
    }

}

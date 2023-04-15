package com.lightdevops.bankaccount.domain.services;

import com.lightdevops.bankaccount.FakeAccountData;
import com.lightdevops.bankaccount.domain.dto.IMapperDto;
import com.lightdevops.bankaccount.domain.dto.TransactionOutputDto;
import com.lightdevops.bankaccount.domain.model.Transaction;
import com.lightdevops.bankaccount.domain.ports.outbound.IFindBankAccountPort;
import com.lightdevops.bankaccount.domain.ports.outbound.IFindTransactionPort;
import com.lightdevops.bankaccount.domain.ports.outbound.ISaveBankAccountPort;
import com.lightdevops.bankaccount.domain.ports.outbound.ISaveTransactionPort;
import org.junit.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@SpringBootTest
public class TransactionServiceTest {
    private final IFindBankAccountPort findBankAccountPort = mock(IFindBankAccountPort.class);
    private final ISaveBankAccountPort saveBankAccountPort = mock(ISaveBankAccountPort.class);
    private final IFindTransactionPort findTransactionPort = mock(IFindTransactionPort.class);
    private final ISaveTransactionPort saveTransactionPort = mock(ISaveTransactionPort.class);
    private final IMapperDto mapperDto = Mappers.getMapper(IMapperDto.class);
    private final BankAccountService bankAccountService = new BankAccountService(findBankAccountPort, saveBankAccountPort);
    private final TransactionService transactionService = new TransactionService(findTransactionPort, saveTransactionPort, mapperDto, bankAccountService);
    private final FakeAccountData fakeAccountData = new FakeAccountData();
    private final Long id = fakeAccountData.id;
    private final List<Transaction> transactions = fakeAccountData.transactions;

    @Test
    public void shouldShowAllTransactions() {
        //Given
        given(findTransactionPort.findTransactionByBankAccountId(id)).willReturn(transactions);

        //When
        List<TransactionOutputDto> allTransactions = transactionService.searchTransactionByBankAccountId(id);

        //Then
        assert transactions.stream().map(Transaction::getId).toList()
                           .equals(allTransactions.stream().map(TransactionOutputDto::getId).toList());
    }
}

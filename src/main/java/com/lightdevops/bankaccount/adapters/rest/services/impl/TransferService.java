package com.lightdevops.bankaccount.adapters.rest.services.impl;

import com.lightdevops.bankaccount.adapters.rest.dtos.IMapperRest;
import com.lightdevops.bankaccount.adapters.rest.dtos.TransactionReq;
import com.lightdevops.bankaccount.adapters.rest.dtos.TransactionRes;
import com.lightdevops.bankaccount.adapters.rest.services.ITransferService;
import com.lightdevops.bankaccount.domain.dto.TransactionInputDto;
import com.lightdevops.bankaccount.domain.dto.TransactionOutputDto;
import com.lightdevops.bankaccount.domain.ports.inbound.IDepositUseCase;
import com.lightdevops.bankaccount.domain.ports.inbound.ISearchTransactionUseCase;
import com.lightdevops.bankaccount.domain.ports.inbound.IWithdrawUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TransferService implements ITransferService {
    private final IMapperRest mapperRest;
    private final IWithdrawUseCase withdrawUseCase;
    private final IDepositUseCase depositUseCase;
    private final ISearchTransactionUseCase searchTransactionUseCase;

    @Override
    public TransactionRes deposit(TransactionReq transactionReq) {
        TransactionInputDto transactionInputDto = mapperRest.reqToDto(transactionReq);
        TransactionOutputDto transactionOutputDto = depositUseCase.deposit(transactionInputDto);
        return mapperRest.dtoToRes(transactionOutputDto);
    }

    @Override
    public List<TransactionRes> findTransactionsByBankCountId(Long bankAccountId) {
        List<TransactionOutputDto> transactions = searchTransactionUseCase.searchTransactionByBankAccountId(bankAccountId);
        return transactions.stream().map(mapperRest::dtoToRes).toList();
    }

    @Override
    public TransactionRes withdraw(TransactionReq transactionReq) {
        TransactionInputDto transactionInputDto = mapperRest.reqToDto(transactionReq);
        TransactionOutputDto transactionOutputDto = withdrawUseCase.withdraw(transactionInputDto);
        return mapperRest.dtoToRes(transactionOutputDto);
    }
}

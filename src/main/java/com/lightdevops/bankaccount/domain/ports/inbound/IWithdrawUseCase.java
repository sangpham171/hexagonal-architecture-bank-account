package com.lightdevops.bankaccount.domain.ports.inbound;

import com.lightdevops.bankaccount.domain.dto.TransactionInputDto;
import com.lightdevops.bankaccount.domain.dto.TransactionOutputDto;
import jakarta.transaction.Transactional;


@Transactional(rollbackOn = Exception.class)
public interface IWithdrawUseCase {
    TransactionOutputDto withdraw(TransactionInputDto transactionInputDto);
}

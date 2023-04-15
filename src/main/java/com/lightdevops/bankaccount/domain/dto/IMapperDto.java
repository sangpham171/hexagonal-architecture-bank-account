package com.lightdevops.bankaccount.domain.dto;

import com.lightdevops.bankaccount.domain.model.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IMapperDto {
    Transaction inputDtoToModel(TransactionInputDto transactionInputDto);

    TransactionOutputDto modelToOutputDto(Transaction transaction);
}

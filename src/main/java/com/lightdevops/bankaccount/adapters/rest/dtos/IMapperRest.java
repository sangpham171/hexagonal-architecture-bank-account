package com.lightdevops.bankaccount.adapters.rest.dtos;

import com.lightdevops.bankaccount.domain.dto.TransactionInputDto;
import com.lightdevops.bankaccount.domain.dto.TransactionOutputDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IMapperRest {
    TransactionRes dtoToRes(TransactionOutputDto transactionOutputDto);

    TransactionInputDto reqToDto(TransactionReq transactionReq);
}

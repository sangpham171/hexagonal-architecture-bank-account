package com.lightdevops.bankaccount.domain.dto;

import com.lightdevops.bankaccount.domain.model.enums.TransactionStatus;
import com.lightdevops.bankaccount.domain.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
public class TransactionOutputDto {
    private Long id;
    private Double amount;
    private String currency;
    private String description;
    private Long bankAccountId;
    private TransactionType transactionType;
    private TransactionStatus transactionStatus;
    private LocalDateTime createdDate;
}

package com.lightdevops.bankaccount.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;


@Builder
@Data
@AllArgsConstructor
public class TransactionInputDto {
    @NonNull
    private Double amount;
    @NonNull
    private String currency;
    @NonNull
    private Long bankAccountId;
    private String description;
}

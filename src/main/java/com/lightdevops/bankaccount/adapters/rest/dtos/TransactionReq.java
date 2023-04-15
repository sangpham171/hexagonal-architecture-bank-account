package com.lightdevops.bankaccount.adapters.rest.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
@Data
@AllArgsConstructor
public class TransactionReq {
    @NonNull
    private Double amount;
    @NonNull
    private String currency;
    @NonNull
    private Long bankAccountId;
    private String description;
}

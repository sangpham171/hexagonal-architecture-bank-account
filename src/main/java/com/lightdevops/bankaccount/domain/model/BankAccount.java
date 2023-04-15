package com.lightdevops.bankaccount.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount {
    private Long id;
    private Long customerId;
    private Double availableBalance; // funds that are available for withdrawal
    private Double pendingBalance; // funds that are not available for withdrawal
    private Integer withdrawLimitPerWeek;
    private Integer currentWithdrawPerWeek;
}

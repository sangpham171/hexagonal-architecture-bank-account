package com.lightdevops.bankaccount.adapters.persistence.entities;

import jakarta.persistence.*;
import lombok.*;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bank_account")
public class BankAccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long customerId;

    // funds that are available for withdrawal
    private Double availableBalance;

    // funds that are not available for withdrawal
    private Double pendingBalance;

    private Integer withdrawLimitPerWeek;
    private Integer currentWithdrawPerWeek;
}

package com.lightdevops.bankaccount.adapters.rest.controllers;

import com.lightdevops.bankaccount.adapters.rest.services.ISecurityService;
import com.lightdevops.bankaccount.domain.ports.inbound.ISearchBalanceUseCase;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/bank-accounts")
public class BankAccountController {
    private final ISearchBalanceUseCase searchBalanceUseCase;
    private final ISecurityService securityService;

    @GetMapping("/{accountId}/available-balance")
    public ResponseEntity<Double> consultBalance(@Valid @PathVariable Long accountId, HttpServletRequest request) {
        securityService.checkSearchBalanceAuthorization(request);

        Double availableBalance = searchBalanceUseCase.searchAvailableBalanceByBankAccountId(accountId);
        return ResponseEntity.ok(availableBalance);
    }

}

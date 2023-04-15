package com.lightdevops.bankaccount.adapters.rest.controllers;

import com.lightdevops.bankaccount.adapters.rest.dtos.TransactionReq;
import com.lightdevops.bankaccount.adapters.rest.dtos.TransactionRes;
import com.lightdevops.bankaccount.adapters.rest.services.ISecurityService;
import com.lightdevops.bankaccount.adapters.rest.services.ITransferService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    private final ISecurityService securityService;
    private final ITransferService transferService;

    @GetMapping("/{bankAccountId}")
    public ResponseEntity<List<TransactionRes>> consultAllTransaction(@Valid @PathVariable Long bankAccountId,
                                                                      HttpServletRequest request) {
        securityService.checkSearchTransactionsAuthorization(request);

        List<TransactionRes> transactionResList = transferService.findTransactionsByBankCountId(bankAccountId);
        HttpStatus status = transactionResList.size() == 0 ? HttpStatus.NOT_FOUND : HttpStatus.OK;

        return ResponseEntity.status(status).body(transactionResList);
    }

    @PutMapping("/deposit")
    public ResponseEntity<TransactionRes> deposit(@Valid @RequestBody TransactionReq transactionReq,
                                                  HttpServletRequest request) {
        securityService.checkDepositAuthorization(request);

        TransactionRes transactionRes = transferService.deposit(transactionReq);
        return ResponseEntity.ok(transactionRes);
    }

    @PutMapping("/withdraw")
    public ResponseEntity<TransactionRes> withdraw(@Valid @RequestBody TransactionReq transactionReq,
                                                   HttpServletRequest request) {
        securityService.checkWithdrawAuthorization(request);

        TransactionRes transactionRes = transferService.withdraw(transactionReq);
        return ResponseEntity.ok(transactionRes);
    }
}

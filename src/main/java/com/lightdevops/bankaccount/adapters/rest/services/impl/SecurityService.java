package com.lightdevops.bankaccount.adapters.rest.services.impl;

import com.lightdevops.bankaccount.adapters.rest.exceptions.AuthException;
import com.lightdevops.bankaccount.adapters.rest.services.ISecurityService;
import com.lightdevops.bankaccount.domain.exceptions.BankAccountExceptionMessage;
import com.lightdevops.bankaccount.domain.exceptions.TransactionExceptionMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class SecurityService implements ISecurityService {

    @Override
    public boolean checkAuthentication(HttpServletRequest request) {
        return request.getHeader("Authorization") != null;
    }

    @Override
    public boolean checkDepositAuthorization(HttpServletRequest request) {
        checkUser(request, TransactionExceptionMessage.FORBIDDEN_DEPOSIT);
        return true;
    }

    @Override
    public boolean checkSearchBalanceAuthorization(HttpServletRequest request) {
        checkUser(request, BankAccountExceptionMessage.FORBIDDEN_SEARCH_BALANCE);
        return true;
    }

    @Override
    public boolean checkSearchTransactionsAuthorization(HttpServletRequest request) {
        checkUser(request, TransactionExceptionMessage.FORBIDDEN_SEARCH_TRANSACTIONS);
        return true;
    }

    @Override
    public boolean checkWithdrawAuthorization(HttpServletRequest request) {
        checkUser(request, TransactionExceptionMessage.FORBIDDEN_WITHDRAW);
        return true;
    }

    private void checkUser(HttpServletRequest request, String exceptionMessage) {
        if (request.getHeader("X-User") == null)
            throw new AuthException(exceptionMessage);
    }
}

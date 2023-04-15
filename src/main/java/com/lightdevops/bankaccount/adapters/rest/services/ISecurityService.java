package com.lightdevops.bankaccount.adapters.rest.services;

import jakarta.servlet.http.HttpServletRequest;

public interface ISecurityService {
    boolean checkAuthentication(HttpServletRequest request);

    boolean checkDepositAuthorization(HttpServletRequest request);

    boolean checkSearchBalanceAuthorization(HttpServletRequest request);

    boolean checkSearchTransactionsAuthorization(HttpServletRequest request);

    boolean checkWithdrawAuthorization(HttpServletRequest request);
}

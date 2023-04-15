package com.lightdevops.bankaccount.adapters.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lightdevops.bankaccount.FakeAccountData;
import com.lightdevops.bankaccount.adapters.persistence.entities.BankAccountEntity;
import com.lightdevops.bankaccount.adapters.persistence.postgres.repositories.BankAccountRepository;
import com.lightdevops.bankaccount.adapters.rest.dtos.TransactionReq;
import com.lightdevops.bankaccount.domain.exceptions.BankAccountExceptionMessage;
import com.lightdevops.bankaccount.domain.exceptions.TransactionExceptionMessage;
import com.lightdevops.bankaccount.domain.model.enums.TransactionStatus;
import com.lightdevops.bankaccount.domain.model.enums.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class WithdrawTest {
    private final String url = "/api/v1/transactions/withdraw";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BankAccountRepository bankAccountRepository;
    private TransactionReq transactionReq;
    private BankAccountEntity bankAccountEntity;
    private Long id;
    private FakeAccountData fakeAccountData;

    @BeforeEach
    public void setUp() {
        fakeAccountData = new FakeAccountData();
        transactionReq = fakeAccountData.transactionReq;
        bankAccountEntity = fakeAccountData.bankAccountEntity;
        id = fakeAccountData.id;
    }

    @Test
    public void withdrawMoreThanAvailableShouldShowBadRequest() throws Exception {
        transactionReq.setAmount(bankAccountEntity.getAvailableBalance() + 1);

        Mockito.when(bankAccountRepository.findById(id))
               .thenReturn(Optional.of(bankAccountEntity));

        mockMvc.perform(MockMvcRequestBuilders.put(url)
                                              .header("Authorization", "Bearer ")
                                              .header("X-User", "1")
                                              .content(new ObjectMapper().writeValueAsString(transactionReq))
                                              .contentType("application/json"))
               .andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.jsonPath("$")
                                               .value(BankAccountExceptionMessage.INSUFFICIENT_BALANCE));

    }

    @Test
    public void withdrawMoreThanLimitWeekShouldShowBadRequest() throws Exception {
        transactionReq.setAmount(
                (double) fakeAccountData.withdrawLimitPerWeek - fakeAccountData.currentWithdrawPerWeek + 1);

        Mockito.when(bankAccountRepository.findById(id))
               .thenReturn(Optional.of(bankAccountEntity));

        mockMvc.perform(MockMvcRequestBuilders.put(url)
                                              .header("Authorization", "Bearer ")
                                              .header("X-User", "1")
                                              .content(new ObjectMapper().writeValueAsString(transactionReq))
                                              .contentType("application/json"))
               .andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.jsonPath("$")
                                               .value(BankAccountExceptionMessage.EXCEED_WITHDRAW_LIMIT_PER_WEEK));

    }

    @Test
    public void withdrawNoAuthorizationShouldShowUnauthorizedStatus() throws Exception {
        Mockito.when(bankAccountRepository.findById(id))
               .thenReturn(Optional.of(bankAccountEntity));

        mockMvc.perform(MockMvcRequestBuilders.put(url)
                                              .header("X-User", "1")
                                              .content(new ObjectMapper().writeValueAsString(transactionReq))
                                              .contentType("application/json"))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());

    }

    @Test
    public void withdrawNoXUserShouldShowForbiddenStatus() throws Exception {
        Mockito.when(bankAccountRepository.findById(id))
               .thenReturn(Optional.of(bankAccountEntity));

        mockMvc.perform(MockMvcRequestBuilders.put(url)
                                              .header("Authorization", "Bearer ")
                                              .content(new ObjectMapper().writeValueAsString(transactionReq))
                                              .contentType("application/json"))
               .andExpect(MockMvcResultMatchers.status().isForbidden())
               .andExpect(MockMvcResultMatchers.jsonPath("$")
                                               .value(TransactionExceptionMessage.FORBIDDEN_WITHDRAW));

    }

    @Test
    public void withdrawShouldSuccess() throws Exception {
        Mockito.when(bankAccountRepository.findById(id))
               .thenReturn(Optional.of(bankAccountEntity));

        mockMvc.perform(MockMvcRequestBuilders.put(url)
                                              .header("Authorization", "Bearer ")
                                              .header("X-User", "1")
                                              .content(new ObjectMapper().writeValueAsString(transactionReq))
                                              .contentType("application/json"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
               .andExpect(MockMvcResultMatchers.jsonPath("$.amount")
                                               .value(transactionReq.getAmount()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.currency")
                                               .value(transactionReq.getCurrency()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.bankAccountId")
                                               .value(transactionReq.getBankAccountId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.description")
                                               .value(transactionReq.getDescription()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.transactionType")
                                               .value(TransactionType.WITHDRAW.toString()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.transactionStatus")
                                               .value(TransactionStatus.SUCCESS.toString()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.createdDate").exists());

    }

    @Test
    public void withdrawWrongBankAccountShouldShowBadRequest() throws Exception {
        transactionReq.setBankAccountId(0L);

        Mockito.when(bankAccountRepository.findById(id))
               .thenReturn(Optional.of(bankAccountEntity));


        mockMvc.perform(MockMvcRequestBuilders.put(url)
                                              .header("Authorization", "Bearer ")
                                              .header("X-User", "1")
                                              .content(new ObjectMapper().writeValueAsString(transactionReq))
                                              .contentType("application/json"))
               .andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.jsonPath("$")
                                               .value(TransactionExceptionMessage.INVALID_TRANSACTION_INPUT));

    }

    @Test
    public void withdrawWrongBankAccountShouldShowNotFound() throws Exception {
        Mockito.when(bankAccountRepository.findById(id))
               .thenReturn(Optional.empty());

        String url = "/api/v1/transactions/withdraw";
        mockMvc.perform(MockMvcRequestBuilders.put(url)
                                              .header("Authorization", "Bearer ")
                                              .header("X-User", "1")
                                              .content(new ObjectMapper().writeValueAsString(transactionReq))
                                              .contentType("application/json"))
               .andExpect(MockMvcResultMatchers.status().isNotFound())
               .andExpect(MockMvcResultMatchers.jsonPath("$")
                                               .value(BankAccountExceptionMessage.ACCOUNT_NOT_FOUND));

    }

    @Test
    public void withdrawZeroShouldShowBadRequest() throws Exception {
        transactionReq.setAmount(0.0);

        Mockito.when(bankAccountRepository.findById(id))
               .thenReturn(Optional.of(bankAccountEntity));

        String url = "/api/v1/transactions/withdraw";
        mockMvc.perform(MockMvcRequestBuilders.put(url)
                                              .header("Authorization", "Bearer ")
                                              .header("X-User", "1")
                                              .content(new ObjectMapper().writeValueAsString(transactionReq))
                                              .contentType("application/json"))
               .andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.jsonPath("$")
                                               .value(TransactionExceptionMessage.INVALID_TRANSACTION_INPUT));

    }
}

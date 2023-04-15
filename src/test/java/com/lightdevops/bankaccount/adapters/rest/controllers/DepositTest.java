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
public class DepositTest {
    private final String url = "/api/v1/transactions/deposit";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BankAccountRepository bankAccountRepository;
    private TransactionReq transactionReq;
    private BankAccountEntity bankAccountEntity;
    private Long id;

    @BeforeEach
    public void setUp() {
        FakeAccountData fakeAccountData = new FakeAccountData();
        transactionReq = fakeAccountData.transactionReq;
        bankAccountEntity = fakeAccountData.bankAccountEntity;
        id = fakeAccountData.id;
    }

    @Test
    public void depositNegativeAmountShouldShowBadRequestStatus() throws Exception {
        transactionReq.setAmount(-1.0);

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
    public void depositWrongAccountShouldShowBadRequestStatus() throws Exception {
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
    public void depositWithoutXUserShouldShowForbiddenStatus() throws Exception {
        Mockito.when(bankAccountRepository.findById(id))
               .thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.put(url)
                                              .header("Authorization", "Bearer ")
                                              .content(new ObjectMapper().writeValueAsString(transactionReq))
                                              .contentType("application/json"))
               .andExpect(MockMvcResultMatchers.status().isForbidden())
               .andExpect(MockMvcResultMatchers.jsonPath("$")
                                               .value(TransactionExceptionMessage.FORBIDDEN_DEPOSIT));

    }

    @Test
    public void depositWithoutAuthorizationShouldShowUnauthorizedStatus() throws Exception {
        Mockito.when(bankAccountRepository.findById(id))
               .thenReturn(Optional.of(bankAccountEntity));

        mockMvc.perform(MockMvcRequestBuilders.put(url)
                                              .header("X-User", "1")
                                              .content(new ObjectMapper().writeValueAsString(transactionReq))
                                              .contentType("application/json"))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void depositShouldSuccess() throws Exception {
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
                                               .value(TransactionType.DEPOSIT.toString()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.transactionStatus")
                                               .value(TransactionStatus.SUCCESS.toString()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.createdDate").exists());

    }

    @Test
    public void depositWrongBankAccountShouldShowNotFound() throws Exception {
        Mockito.when(bankAccountRepository.findById(id))
               .thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.put(url)
                                              .header("Authorization", "Bearer ")
                                              .header("X-User", "1")
                                              .content(new ObjectMapper().writeValueAsString(transactionReq))
                                              .contentType("application/json"))
               .andExpect(MockMvcResultMatchers.status().isNotFound())
               .andExpect(MockMvcResultMatchers.jsonPath("$")
                                               .value(BankAccountExceptionMessage.ACCOUNT_NOT_FOUND));

    }

}

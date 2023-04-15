package com.lightdevops.bankaccount.adapters.rest.controllers;

import com.lightdevops.bankaccount.FakeAccountData;
import com.lightdevops.bankaccount.adapters.persistence.entities.TransactionEntity;
import com.lightdevops.bankaccount.adapters.persistence.postgres.repositories.TransactionRepository;
import com.lightdevops.bankaccount.domain.exceptions.BankAccountExceptionMessage;
import com.lightdevops.bankaccount.domain.exceptions.TransactionExceptionMessage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class SearchTransactionsTest {
    private final FakeAccountData fakeAccountData = new FakeAccountData();
    private final List<TransactionEntity> transactionEntities = fakeAccountData.transactionEntities;
    private final Long id = fakeAccountData.id;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TransactionRepository transactionRepository;

    @Test
    public void searchInvalidIdShouldShowBadRequestStatus() throws Exception {
        String url = "/api/v1/transactions/-1";
        mockMvc.perform(MockMvcRequestBuilders.get(url)
                                              .header("Authorization", "Bearer ")
                                              .header("X-User", "1"))
               .andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.jsonPath("$")
                                               .value(BankAccountExceptionMessage.INVALID_ACCOUNT_ID));
    }

    @Test
    public void searchWithoutXUserShouldShowForbiddenStatus() throws Exception {
        Mockito.when(transactionRepository.findByBankAccountId(id))
               .thenReturn(transactionEntities);

        String url = "/api/v1/transactions/1";
        mockMvc.perform(MockMvcRequestBuilders.get(url).header("Authorization", "Bearer "))
               .andExpect(MockMvcResultMatchers.status().isForbidden())
               .andExpect(MockMvcResultMatchers.jsonPath("$")
                                               .value(TransactionExceptionMessage.FORBIDDEN_SEARCH_TRANSACTIONS));
    }

    @Test
    public void searchWithoutAuthorizationShouldShowUnauthorizedStatus() throws Exception {
        Mockito.when(transactionRepository.findByBankAccountId(id))
               .thenReturn(transactionEntities);

        String url = "/api/v1/transactions/1";
        mockMvc.perform(MockMvcRequestBuilders.get(url).header("X-User", "1"))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void searchTransactionListShouldSuccess() throws Exception {
        Mockito.when(transactionRepository.findByBankAccountId(id))
               .thenReturn(transactionEntities);

        String url = "/api/v1/transactions/1";
        mockMvc.perform(MockMvcRequestBuilders.get(url)
                                              .header("Authorization", "Bearer ")
                                              .header("X-User", "1"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].id")
                                               .value(transactionEntities.get(0).getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].amount")
                                               .value(transactionEntities.get(0).getAmount()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].currency")
                                               .value(transactionEntities.get(0).getCurrency()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].description")
                                               .value(transactionEntities.get(0).getDescription()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].bankAccountId")
                                               .value(transactionEntities.get(0).getBankAccountId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].transactionStatus")
                                               .value(transactionEntities.get(0).getTransactionStatus().toString()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].transactionType")
                                               .value(transactionEntities.get(0).getTransactionType().toString()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].createdDate")
                                               .value(transactionEntities.get(0).getCreatedDate().toString()));
    }
}

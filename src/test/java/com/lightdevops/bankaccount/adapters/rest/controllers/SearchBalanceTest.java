package com.lightdevops.bankaccount.adapters.rest.controllers;

import com.lightdevops.bankaccount.FakeAccountData;
import com.lightdevops.bankaccount.adapters.persistence.entities.BankAccountEntity;
import com.lightdevops.bankaccount.adapters.persistence.postgres.repositories.BankAccountRepository;
import com.lightdevops.bankaccount.domain.exceptions.BankAccountExceptionMessage;
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
public class SearchBalanceTest {
    private final FakeAccountData fakeAccountData = new FakeAccountData();
    private final BankAccountEntity bankAccountEntity = fakeAccountData.bankAccountEntity;
    private final Double availableBalance = fakeAccountData.availableBalance;
    private final Long id = fakeAccountData.id;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BankAccountRepository bankAccountRepository;

    @Test
    public void canNotFindBankAccountShouldShowNotFound() throws Exception {
        Mockito.when(bankAccountRepository.findById(id))
               .thenReturn(Optional.empty());

        String url = "/api/v1/bank-accounts/2/available-balance";
        mockMvc.perform(MockMvcRequestBuilders.get(url)
                                              .header("Authorization", "Bearer ")
                                              .header("X-User", "1"))
               .andExpect(MockMvcResultMatchers.status().isNotFound())
               .andExpect(MockMvcResultMatchers.jsonPath("$")
                                               .value(BankAccountExceptionMessage.ACCOUNT_NOT_FOUND));

    }

    @Test
    public void searchNotValidIdShouldShowBadRequestStatus() throws Exception {
        String url = "/api/v1/bank-accounts/-1/available-balance";
        mockMvc.perform(MockMvcRequestBuilders.get(url)
                                              .header("Authorization", "Bearer ")
                                              .header("X-User", "1"))
               .andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.jsonPath("$")
                                               .value(BankAccountExceptionMessage.INVALID_ACCOUNT_ID));
    }

    @Test
    public void searchBalanceWithoutXUserShouldShowForbiddenStatus() throws Exception {
        Mockito.when(bankAccountRepository.findById(id))
               .thenReturn(Optional.of(bankAccountEntity));

        String url = "/api/v1/bank-accounts/1/available-balance";
        mockMvc.perform(MockMvcRequestBuilders.get(url).header("Authorization", "Bearer "))
               .andExpect(MockMvcResultMatchers.status().isForbidden())
               .andExpect(MockMvcResultMatchers.jsonPath("$")
                                               .value(BankAccountExceptionMessage.FORBIDDEN_SEARCH_BALANCE));

    }

    @Test
    public void searchBalanceWithouAuthorizationShouldShowUnauthorizedStatus() throws Exception {
        Long id = 1L;

        Mockito.when(bankAccountRepository.findById(id))
               .thenReturn(Optional.of(bankAccountEntity));

        String url = "/api/v1/bank-accounts/1/available-balance";
        mockMvc.perform(MockMvcRequestBuilders.get(url).header("X-User", "1"))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());

    }

    @Test
    public void searchBalanceShouldSuccess() throws Exception {
        Long id = 1L;

        Mockito.when(bankAccountRepository.findById(id))
               .thenReturn(Optional.of(bankAccountEntity));

        String url = "/api/v1/bank-accounts/1/available-balance";
        mockMvc.perform(MockMvcRequestBuilders.get(url)
                                              .header("Authorization", "Bearer ")
                                              .header("X-User", "1"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$").value(availableBalance));

    }


}

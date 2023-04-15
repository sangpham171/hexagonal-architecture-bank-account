package com.lightdevops.bankaccount.config;

import com.lightdevops.bankaccount.adapters.persistence.postgres.FindBankAccountPostgresAdapter;
import com.lightdevops.bankaccount.adapters.persistence.postgres.FindTransactionPostgresAdapter;
import com.lightdevops.bankaccount.adapters.persistence.postgres.SaveBankAccountPostgresAdapter;
import com.lightdevops.bankaccount.adapters.persistence.postgres.SaveTransactionPostgresAdapter;
import com.lightdevops.bankaccount.domain.dto.IMapperDto;
import com.lightdevops.bankaccount.domain.services.BankAccountService;
import com.lightdevops.bankaccount.domain.services.TransactionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    BankAccountService bankAccountService(final FindBankAccountPostgresAdapter findBankAccountPostgresAdapter,
                                          final SaveBankAccountPostgresAdapter saveBankAccountPostgresAdapter) {
        return new BankAccountService(findBankAccountPostgresAdapter, saveBankAccountPostgresAdapter);
    }

    @Bean
    TransactionService transactionService(final FindTransactionPostgresAdapter findTransactionPostgresAdapter,
                                          final SaveTransactionPostgresAdapter saveTransactionPostgresAdapter,
                                          final IMapperDto mapperDto,
                                          final BankAccountService bankAccountService) {
        return new TransactionService(findTransactionPostgresAdapter, saveTransactionPostgresAdapter, mapperDto, bankAccountService);
    }


}

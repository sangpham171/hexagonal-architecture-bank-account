package com.lightdevops.bankaccount;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BankAccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankAccountApplication.class, args);
    }

}

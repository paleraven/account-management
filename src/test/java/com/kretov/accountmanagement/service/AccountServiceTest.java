package com.kretov.accountmanagement.service;

import com.kretov.accountmanagement.entity.Account;
import com.kretov.accountmanagement.repository.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.springframework.test.util.AssertionErrors.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountServiceTest {
    private static final Long ACCOUNT_ID = 100500L;
    private static final Double MONEY = 500.0;

    @Autowired
    private AccountService accountService;

    @MockBean
    private AccountRepository accountRepository;

    @TestConfiguration
    static class AccountServiceImplTestContextConfiguration {

        @Bean
        public AccountService accountService() {
            return new AccountService();
        }
    }

    @Before
    public void setUp() {
        Account account = new Account();
        account.setId(ACCOUNT_ID);
        account.setMoney(MONEY);

        Mockito.when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
    }

    @Test
    public void whenValidId_thenAccountShouldBeFound() {
        Account found = accountService.findById(ACCOUNT_ID);

        assertTrue("id is incorrect", ACCOUNT_ID.equals(found.getId()));
    }

    @Test
    public void depositMoney() {
        accountService.depositMoney(accountService.findById(ACCOUNT_ID), 1.0);
        Account found = accountService.findById(ACCOUNT_ID);

        assertTrue("incorrect money", found.getMoney().equals(501.0));
    }

    @Test
    public void withdrawMoney_success() {
        boolean result = accountService.withdrawMoney(accountService.findById(ACCOUNT_ID), 1.0);
        Account found = accountService.findById(ACCOUNT_ID);

        assertTrue("withdraw failed", result);
        assertTrue("incorrect money", found.getMoney().equals(499.0));
    }

    @Test
    public void withdrawMoney_error() {
        boolean result = accountService.withdrawMoney(accountService.findById(ACCOUNT_ID), 501.0);
        Account found = accountService.findById(ACCOUNT_ID);

        //если снимаем слишком много денег, то result должен быть false и сумма на счету не должна измениться
        assertTrue("withdraw success", !result);
        assertTrue("incorrect money", found.getMoney().equals(500.0));
    }

    @Test
    public void transferMoney_success() {
        //Создадим еще один счет (получения)
        final Long destinationAccountId = 100501L;
        Account account = new Account();
        account.setId(destinationAccountId);
        account.setMoney(MONEY);
        Mockito.when(accountRepository.findById(destinationAccountId)).thenReturn(Optional.of(account));

        boolean result = accountService.transferMoney(accountService.findById(ACCOUNT_ID), accountService.findById(destinationAccountId), 1.0);
        Account foundSourceAccount = accountService.findById(ACCOUNT_ID);
        Account foundDestinationAccount = accountService.findById(destinationAccountId);

        assertTrue("transfer failed", result);
        assertTrue("incorrect money", foundSourceAccount.getMoney().equals(499.0));
        assertTrue("incorrect money", foundDestinationAccount.getMoney().equals(501.0));
    }

    @Test
    public void transferMoney_error() {
        //Создадим еще один счет (получения)
        final Long destinationAccountId = 100501L;
        Account account = new Account();
        account.setId(destinationAccountId);
        account.setMoney(MONEY);
        Mockito.when(accountRepository.findById(destinationAccountId)).thenReturn(Optional.of(account));

        boolean result = accountService.transferMoney(accountService.findById(ACCOUNT_ID), accountService.findById(destinationAccountId), 501.0);
        Account foundSourceAccount = accountService.findById(ACCOUNT_ID);
        Account foundDestinationAccount = accountService.findById(destinationAccountId);

        //если снимаем слишком много денег, то result должен быть false и сумма на счетах не должна измениться
        assertTrue("transfer success", !result);
        assertTrue("incorrect money", foundSourceAccount.getMoney().equals(500.0));
        assertTrue("incorrect money", foundDestinationAccount.getMoney().equals(500.0));
    }
}

package com.kretov.accountmanagement.controller;

import com.kretov.accountmanagement.dto.AccountDto;
import com.kretov.accountmanagement.entity.Account;
import com.kretov.accountmanagement.entity.Customer;
import com.kretov.accountmanagement.response.Response;
import com.kretov.accountmanagement.service.AccountService;
import com.kretov.accountmanagement.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.kretov.accountmanagement.response.Response.illegalFormatResponse;
import static com.kretov.accountmanagement.response.Status.ERROR;
import static com.kretov.accountmanagement.response.Status.SUCCESS;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CustomerService customerService;

    /**
     * Получить все банковские счета
     *
     * @return json со всеми счетами
     */
    public Response<Account> getAllAccounts() {
        List<Account> accounts = accountService.findAllAccounts();
        if (accounts.isEmpty()) {
            return new Response<>(ERROR, "No accounts", Collections.emptyList());
        }
        return new Response<>(SUCCESS,"Found " + accounts.size() + " accounts", accounts);
    }

    /**
     * Получить все счета конкретного клиента
     *
     * @param id клиент
     * @return все счета клиента или сообщение, что клиент некорректный
     */
    public Response<Account> getAccountsByCustomerId(String id) {
        try {
            Long customerId = Long.valueOf(id);
            Customer customer = customerService.findById(customerId);
            if (customer != null) {
                List<Account> accounts = accountService.findByCustomer(customer);
                if (accounts.isEmpty()) {
                    return new Response<>(ERROR, "No accounts for customer " + customer.getId(), Collections.emptyList());
                }
                return new Response<>(SUCCESS,"Found " + accounts.size() + " accounts", accounts);
            }
            return new Response<>(ERROR, "Illegal customer id", Collections.emptyList());
        } catch (NumberFormatException e) {
            return illegalFormatResponse();
        }
    }

    /**
     * Получить конкретный счет
     *
     * @param id счет
     * @return информация по счету или сообщение, что счет некорректный
     */
    public Response<Account> getAccountByAccountId(String id) {
        try {
            Long accountId = Long.valueOf(id);
            Account account = accountService.findById(accountId);
            if (account != null) {
                return new Response<>(SUCCESS, "Account with id " + id + " was found", Collections.singletonList(account));
            }
            return new Response<>(ERROR, "No accounts", Collections.emptyList());
        } catch (NumberFormatException e) {
            return illegalFormatResponse();
        }
    }

    /**
     * Удалить счет
     * @param id идентификатор
     * @return Статус операции
     */
    public Response<Account> deleteAccountByAccountId(String id) {
        try {
            Long accountId = Long.valueOf(id);
            Account account = accountService.findById(accountId);
            if (account != null) {
                accountService.deleteById(accountId);
                return new Response<>(SUCCESS,"Account with id " + id + " was deleted", Collections.emptyList());
            }
            return new Response<>(ERROR,"Illegal account id", Collections.emptyList());
        } catch (NumberFormatException e) {
            return illegalFormatResponse();
        }
    }

    /**
     * Создать счет
     * @param newAccount счет
     * @return Статус операции
     */
    public Response<Account> createAccount(Account newAccount) {
        try {
            accountService.save(newAccount);
            return new Response<>(SUCCESS, "Created account with id " + newAccount.getId(), Collections.singletonList(newAccount));
        } catch (Exception e) {
            return new Response<>(ERROR,"Account wasn't created", Collections.emptyList());
        }
    }

    /**
     * Создать счет
     * @param customerId id клиента
     * @param money сумма на счету
     * @return Статус операции
     */
    public Response<Account> createAccount(Long customerId, Double money) {
        try {
            if (money < 0) {
                return new Response<>(ERROR,"Illegal count of money. The money must be a positive number", Collections.emptyList());
            } else {
                Customer customer = customerService.findById(customerId);
                if (customer != null) {
                    Account account = new Account();
                    account.setCustomer(customer);
                    account.setMoney(money);
                    Account savedAccount = accountService.save(account);
                    return new Response<>(SUCCESS, "Created account with id " + savedAccount.getId(), Collections.singletonList(savedAccount));
                }
            }
            return new Response<>(ERROR,"Customer doesn't exist", Collections.emptyList());
        } catch (Exception e) {
            return new Response<>(ERROR,"Account wasn't created", Collections.emptyList());
        }
    }

    /**
     * Обновить клиента
     * @param account клиент
     * @return Статус операции
     */
    public Response<Account> updateAccount(Account account) {
        try {
            if (account.getMoney() < 0) {
                return new Response<>(ERROR,"Illegal count of money. The money must be a positive number", Collections.emptyList());
            }
            if (accountService.findById(account.getId()) != null) {
                accountService.save(account);
                return new Response<>(SUCCESS, "Updated account with id " + account.getId(), Collections.singletonList(account));
            } else {
                return new Response<>(ERROR,"Account doesn't exist", Collections.emptyList());
            }
        } catch (Exception e) {
            return new Response<>(ERROR,"Account wasn't updated", Collections.emptyList());
        }
    }

    /**
     * Обновить существующий счет
     * @param id идентификатор
     * @param updatedAccount новые данные (id клиента и сумма)
     * @return Статус операции
     */
    public Response<Account> updateAccount(String id, AccountDto updatedAccount) {
        try {
            Long accountId = Long.valueOf(id);
            Account account = accountService.findById(accountId);
            if (account != null) {
                if (updatedAccount.getMoney() < 0) {
                    return new Response<>(ERROR,"Illegal count of money. The money must be a positive number", Collections.emptyList());
                }
                Customer customer = customerService.findById(updatedAccount.getCustomerId());
                if (customer != null) {
                    account.setCustomer(customer);
                    account.setMoney(updatedAccount.getMoney());
                    accountService.save(account);
                    return new Response<>(SUCCESS, "Updated account with id " + account.getId(), Collections.singletonList(account));
                }
                return new Response<>(ERROR,"Illegal customer id", Collections.emptyList());
            }
            return new Response<>(ERROR,"Illegal account id", Collections.emptyList());
        } catch (NumberFormatException e) {
            return illegalFormatResponse();
        }
    }

    /**
     * Положить деньги на счет
     *
     * @param id счет
     * @param money сумма пополнения
     * @return json с новым состоянием счета
     */
    public Response<Account> depositMoney(String id, String money) {
        try {
            Long accountId = Long.valueOf(id);
            Account account = accountService.findById(accountId);
            if (account != null) {
                Double deposit = Double.valueOf(money);
                if (deposit < 0) {
                    return new Response<>(ERROR,"The deposit sum must be a positive number", Collections.emptyList());
                }
                accountService.depositMoney(account, deposit);
                return new Response<>(SUCCESS, "Money deposited. Account " + account.getId(), Collections.singletonList(accountService.findById(accountId)));
            }
            return new Response<>(ERROR,"Illegal account id", Collections.emptyList());
        } catch (NumberFormatException e) {
            return illegalFormatResponse();
        }
    }

    /**
     * Снять деньги со счета
     *
     * @param id счет
     * @param money сумма снятия
     * @return Статус операции (могла быть слишком большая сумма снятия) и новое состояние счета
     */
    public Response<Account> withdrawMoney(String id, String money) {
        try {
            Long accountId = Long.valueOf(id);
            Account account = accountService.findById(accountId);
            if (account != null) {
                Double withdraw = Double.valueOf(money);
                if (withdraw < 0) {
                    return new Response<>(ERROR,"The withdraw sum must be a positive number", Collections.emptyList());
                }
                boolean isSuccessWithdraw = accountService.withdrawMoney(account, withdraw);
                if (isSuccessWithdraw) {
                    return new Response<>(SUCCESS, "Successful withdraw. Account " + account.getId(), Collections.singletonList(accountService.findById(accountId)));
                } else {
                    return new Response<>(ERROR,"Withdraw failed. Not enough money", Collections.emptyList());
                }
            }
            return new Response<>(ERROR,"Illegal account id", Collections.emptyList());
        } catch (NumberFormatException e) {
            return illegalFormatResponse();
        }
    }

    /**
     * Перевести со счета на счет
     *
     * @param sourceId счет снятия
     * @param destinationId счет пополнения
     * @param money сумма перевода
     * @return Статус операции (могло быть недостаточно денег на счету) и новые состояния счетов
     */
    public Response<Account> transferMoney(String sourceId, String destinationId, String money) {
        if (sourceId.equals(destinationId)) {
            return new Response<>(ERROR,"Illegal format of input data. Please, use different account ids.", Collections.emptyList());
        }
        try {
            Long sourceAccountId = Long.valueOf(sourceId);
            Long destinationAccountId = Long.valueOf(destinationId);
            Account sourceAccount = accountService.findById(sourceAccountId);
            Account destinationAccount = accountService.findById(destinationAccountId);
            if (sourceAccount != null && destinationAccount != null) {
                Double transfer = Double.valueOf(money);
                if (transfer < 0) {
                    return new Response<>(ERROR,"The transfer sum must be a positive number", Collections.emptyList());
                }
                boolean isSuccessTransfer = accountService.transferMoney(sourceAccount, destinationAccount, transfer);
                if (isSuccessTransfer) {
                    return new Response<>(SUCCESS, "Successful transfer", Arrays.asList(accountService.findById(sourceAccountId), accountService.findById(destinationAccountId)));
                } else {
                    return new Response<>(ERROR,"Transfer failed. Not enough money", Collections.emptyList());
                }
            }
            return new Response<>(ERROR,"Illegal account id", Collections.emptyList());
        } catch (NumberFormatException e) {
            return illegalFormatResponse();
        }
    }
}

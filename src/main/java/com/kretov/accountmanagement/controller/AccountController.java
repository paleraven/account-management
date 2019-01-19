package com.kretov.accountmanagement.controller;

import com.kretov.accountmanagement.dto.AccountDto;
import com.kretov.accountmanagement.entity.Account;
import com.kretov.accountmanagement.entity.Customer;
import com.kretov.accountmanagement.service.AccountService;
import com.kretov.accountmanagement.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/bank")
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
	@GetMapping("/accounts")
	List<String> getAllAccounts() {
		List<Account> accounts = accountService.findAllAccounts();
		return accounts.stream().map(Account::toString).collect(Collectors.toList());
	}

	/**
	 * Получить все счета конкретного клиента
	 *
	 * @param id клиент
	 * @return все счета клиента или сообщение, что клиент некорректный
	 */
	@GetMapping("/accounts/{id}")
	String getAccountsByCustomerId(@PathVariable String id) {
	    try {
            Long customerId = Long.valueOf(id);
            Customer customer = customerService.findById(customerId);
            if (customer != null) {
                List<Account> accounts = accountService.findByCustomer(customer);
                StringBuilder stringBuilder = new StringBuilder();
                for (Account account : accounts) {
                    if (account != null) {
                        stringBuilder.append(account.toString());
                        stringBuilder.append("\n");
                    }
                }
                return stringBuilder.toString();
            }
            return "Illegal customer id";
        } catch (NumberFormatException e) {
	        return "Illegal format of input data. Please, use number.";
        }
	}

	/**
	 * Получить конкретный счет
	 *
	 * @param id счет
	 * @return информация по счету или сообщение, что счет некорректный
	 */
	@GetMapping("/accountInfo/{id}")
	String getAccountByAccountId(@PathVariable String id) {
        try {
            Long accountId = Long.valueOf(id);
            Account account = accountService.findById(accountId);
            if (account != null) {
                return account.toString();
            }
            return "Illegal account id";
        } catch (NumberFormatException e) {
            return "Illegal format of input data. Please, use number.";
        }
    }

	/**
	 * Удалить счет
	 * @param id идентификатор
	 * @return Статус операции
	 */
	@DeleteMapping("/accountDelete/{id}")
	String deleteAccountByAccountId(@PathVariable String id) {
	    try {
            Long accountId = Long.valueOf(id);
            Account account = accountService.findById(accountId);
            if (account != null) {
                accountService.deleteById(accountId);
                return "Account with id " + id + " was deleted";
            }
            return "Operation isn't executed. Illegal account id";
        } catch (NumberFormatException e) {
            return "Operation isn't executed. Illegal format of input data. Please, use number.";
        }
	}

    /**
     * Создать счет
     * @param newAccount dto с id клиента и суммой на счету
     * @return Статус операции
     */
    @PostMapping(value="/accountCreate", consumes = APPLICATION_JSON_VALUE)
    String createAccount(@RequestBody AccountDto newAccount) {
        Account account = new Account();
        account.setCustomer(customerService.findById(newAccount.getCustomerId()));
        account.setMoney(newAccount.getMoney());
        accountService.save(account);
        return "Created account with id " + account.getId();
    }

    /**
     * Обновить существующий счет
     * @param id идентификатор
     * @param updatedAccount новые данные (id клиента и сумма)
     * @return Статус операции
     */
    @PutMapping(value="/accountUpdate/{id}", consumes = APPLICATION_JSON_VALUE)
    String updateAccount(@PathVariable String id, @RequestBody AccountDto updatedAccount) {
        try {
            Long accountId = Long.valueOf(id);
            Account account = accountService.findById(accountId);
            if (account != null) {
                if (updatedAccount.getMoney() < 0) {
                    return "Operation isn't executed. Illegal count of money in json. The money must be a positive number";
                }
                Customer customer = customerService.findById(updatedAccount.getCustomerId());
                if (customer != null) {
                    account.setCustomer(customer);
                    account.setMoney(updatedAccount.getMoney());
                    accountService.save(account);
                    return "Updated account with id " + account.getId();
                }
                return "Operation isn't executed. Illegal customer id in json";
            }
            return "Operation isn't executed. Illegal account id";
        } catch (NumberFormatException e) {
            return "Operation isn't executed. Illegal format of input data. Please, use number.";
        }
    }

	/**
	 * Положить деньги на счет
	 *
	 * @param id счет
	 * @param money сумма пополнения
	 * @return json с новым состоянием счета
	 */
	@PutMapping("/deposit/{id}/{money}")
	String depositMoney(@PathVariable String id, @PathVariable String money) {
	    try {
            Long accountId = Long.valueOf(id);
            Account account = accountService.findById(accountId);
            if (account != null) {
                Double deposit = Double.valueOf(money);
                if (deposit < 0) {
                    return "Operation isn't executed. The deposit sum must be a positive number";
                }
                accountService.depositMoney(account, deposit);
                return accountService.findById(accountId).toString();
            }
            return "Operation isn't executed. Illegal account id";
        } catch (NumberFormatException e) {
            return "Operation isn't executed. Illegal format of input data. Please, use numbers.";
        }
	}

	/**
	 * Снять деньги со счета
	 *
	 * @param id счет
	 * @param money сумма снятия
	 * @return Статус операции (могла быть слишком большая сумма снятия) и новое состояние счета
	 */
	@PutMapping("/withdraw/{id}/{money}")
	String withdrawMoney(@PathVariable String id, @PathVariable String money) {
	    try {
            Long accountId = Long.valueOf(id);
            Account account = accountService.findById(accountId);
            if (account != null) {
                Double withdraw = Double.valueOf(money);
                if (withdraw < 0) {
                    return "Operation isn't executed. The withdraw sum must be a positive number";
                }
                boolean isSuccessWithdraw = accountService.withdrawMoney(account, withdraw);
                if (isSuccessWithdraw) {
                    return "Successful withdraw.\nAccount: " + accountService.findById(accountId).toString();
                } else {
                    return "Withdraw failed. Not enough money";
                }
            }
            return "Operation isn't executed. Illegal account id";
        } catch (NumberFormatException e) {
            return "Operation isn't executed. Illegal format of input data. Please, use numbers.";
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
	@PutMapping("/transfer/{sourceId}/{destinationId}/{money}")
	String transferMoney(@PathVariable String sourceId, @PathVariable String destinationId, @PathVariable String money) {
	    if (sourceId.equals(destinationId)) {
	        return "Operation isn't executed. Illegal format of input data. Please, use different account ids.";
        }
	    try {
            Long sourceAccountId = Long.valueOf(sourceId);
            Long destinationAccountId = Long.valueOf(destinationId);
            Account sourceAccount = accountService.findById(sourceAccountId);
            Account destinationAccount = accountService.findById(destinationAccountId);
            if (sourceAccount != null && destinationAccount != null) {
                Double transfer = Double.valueOf(money);
                if (transfer < 0) {
                    return "Operation isn't executed. The transfer sum must be a positive number";
                }
                boolean isSuccessTransfer = accountService.transferMoney(sourceAccount, destinationAccount, transfer);
                if (isSuccessTransfer) {
                    return "Successful transfer.\nSource account: " + accountService.findById(sourceAccountId).toString() +
                            ".\nDestination account: " + accountService.findById(destinationAccountId).toString();
                } else {
                    return "Transfer failed. Not enough money";
                }
            }
            return "Operation isn't executed. Illegal account id";
        } catch (NumberFormatException e) {
            return "Operation isn't executed. Illegal format of input data. Please, use numbers.";
        }
	}

}

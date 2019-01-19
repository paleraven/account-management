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
		return accounts.stream().map(AccountDto::new).map(AccountDto::toString).collect(Collectors.toList());
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
                        AccountDto accountDto = new AccountDto(account);
                        stringBuilder.append(accountDto.toString());
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
                AccountDto accountDto = new AccountDto(account);
                return accountDto.toString();
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
            return "Illegal account id";
        } catch (NumberFormatException e) {
            return "Illegal format of input data. Please, use number.";
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
                AccountDto accountDto = new AccountDto(accountService.findById(accountId));
                return accountDto.toString();
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
                    AccountDto accountDto = new AccountDto(accountService.findById(accountId));
                    return "Successful withdraw.\nAccount: " + accountDto.toString();
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
                    AccountDto sourceAccountDto = new AccountDto(accountService.findById(sourceAccountId));
                    AccountDto destinationAccountDto = new AccountDto(accountService.findById(destinationAccountId));
                    return "Successful transfer.\nSource account: " + sourceAccountDto.toString() + ".\nDestination account: "
                            + destinationAccountDto.toString();
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

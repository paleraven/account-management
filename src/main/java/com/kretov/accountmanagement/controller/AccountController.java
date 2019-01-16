package com.kretov.accountmanagement.controller;

import static com.kretov.accountmanagement.util.AccountUtil.validateAccount;
import static com.kretov.accountmanagement.util.CustomerUtil.validateCustomer;

import com.kretov.accountmanagement.dto.AccountDto;
import com.kretov.accountmanagement.entity.Account;
import com.kretov.accountmanagement.service.AccountService;
import com.kretov.accountmanagement.service.CustomerService;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private CustomerService customerService;

	/**
	 * Получить все банковские счета
	 * Пример запроса в curl:
	 * curl localhost:9090/accounts
	 * @return json со всеми счетами
	 */
	@GetMapping("/accounts")
	List<String> getAllAccounts() {
		List<Account> accounts = accountService.findAllAccounts();
		return accounts.stream().map(AccountDto::new).map(AccountDto::toString).collect(Collectors.toList());
	}

	/**
	 * Получить все счета конкретного клиента
	 * Пример запроса в curl:
	 * curl localhost:9090/accounts/1
	 * @param id клиент
	 * @return json со всеми счетами клиента
	 */
	@GetMapping("/accounts/{id}")
	List<String> getAccountsByCustomerId(@PathVariable String id) {
		Long customerId = Long.valueOf(id);
		if (validateCustomer(customerService, customerId)) {
			List<Account> accounts = accountService.findByCustomer(customerService.findById(customerId));
			return accounts.stream().map(AccountDto::new).map(AccountDto::toString).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	/**
	 * Получить конкретный счет
	 * Пример запроса в curl:
	 * curl localhost:9090/account/1
	 * @param id счет
	 * @return json конкретного счета
	 */
	@GetMapping("/account/{id}")
	String getAccountByAccountId(@PathVariable String id) {
		Long accountId = Long.valueOf(id);
		if (validateAccount(accountService, accountId)) {
			AccountDto accountDto = new AccountDto(accountService.findById(accountId));
			return accountDto.toString();
		}
		return "Illegal account";
	}

	/**
	 * Положить деньги на счет
	 * Пример запроса в curl:
	 * curl localhost:9090/deposit/1/100
	 * @param id счет
	 * @param money сумма пополнения
	 * @return json с новым состоянием счета
	 */
	@GetMapping("/deposit/{id}/{money}")
	String depositMoney(@PathVariable String id, @PathVariable String money) {
		Long accountId = Long.valueOf(id);
		if (validateAccount(accountService, accountId)) {
			accountService.depositMoney(accountService.findById(accountId), Double.valueOf(money));
			AccountDto accountDto = new AccountDto(accountService.findById(accountId));
			return accountDto.toString();
		}
		return "Illegal account";
	}

	/**
	 * Снять деньги со счета
	 * Пример запроса в curl:
	 * curl localhost:9090/withdraw/1/100
	 * @param id счет
	 * @param money сумма снятия
	 * @return Статус операции (могла быть слишком большая сумма снятия) и новое состояние счета
	 */
	@GetMapping("/withdraw/{id}/{money}")
	String withdrawMoney(@PathVariable String id, @PathVariable String money) {
		Long accountId = Long.valueOf(id);
		if (validateAccount(accountService, accountId)) {
			boolean isSuccessWithdraw = accountService.withdrawMoney(accountService.findById(accountId), Double.valueOf(money));
			if (isSuccessWithdraw) {
				AccountDto accountDto = new AccountDto(accountService.findById(accountId));
				return "Successful withdraw.\nAccount: " + accountDto.toString();
			} else {
				return "Withdraw failed. Not enough money";
			}
		}
		return "Operation isn't executed";
	}

	/**
	 * Перевести со счета на счет
	 * Пример запроса в curl:
	 * curl localhost:9090/transfer/1/2/100
	 * @param sourceId
	 * @param destinationId
	 * @param money
	 * @return Статус операции (могло быть недостаточно денег на счету) и новые состояния счетов
	 */
	@GetMapping("/transfer/{sourceId}/{destinationId}/{money}")
	String transferMoney(@PathVariable String sourceId, @PathVariable String destinationId, @PathVariable String money) {
		Long sourceAccountId = Long.valueOf(sourceId);
		Long destinationAccountId = Long.valueOf(destinationId);
		if (validateAccount(accountService, sourceAccountId) && validateAccount(accountService, destinationAccountId) ) {
			boolean isSuccessTransfer = accountService.transferMoney(accountService.findById(sourceAccountId),
					accountService.findById(destinationAccountId),
					Double.valueOf(money));
			if (isSuccessTransfer) {
				AccountDto sourceAccountDto = new AccountDto(accountService.findById(sourceAccountId));
				AccountDto destinationAccountDto = new AccountDto(accountService.findById(destinationAccountId));
				return "Successful transfer.\nSource account: " + sourceAccountDto.toString() + ".\nDestination account: " + destinationAccountDto.toString();
			} else {
				return "Withdraw failed. Not enough money";
			}
		}
		return "Operation isn't executed";
	}

}

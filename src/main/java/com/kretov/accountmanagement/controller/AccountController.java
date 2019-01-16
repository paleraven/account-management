package com.kretov.accountmanagement.controller;

import static com.kretov.accountmanagement.util.AccountUtil.validateAccount;
import static com.kretov.accountmanagement.util.CustomerUtil.validateCustomer;

import com.kretov.accountmanagement.entity.Account;
import com.kretov.accountmanagement.service.AccountService;
import com.kretov.accountmanagement.service.CustomerService;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/{customerId}")
public class AccountController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private CustomerService customerService;

	@GetMapping("/accounts")
	List<Account> readAccounts (@PathVariable String id) {
		Long customerId = Long.valueOf(id);
		if (validateCustomer(customerService, customerId)) {
			return accountService.findByCustomer(customerService.findById(customerId));
		}
		return Collections.emptyList();
	}

	@GetMapping("/deposit")
	Account depositMoney (@PathVariable String id, @PathVariable String money) {
		Long accountId = Long.valueOf(id);
		if (validateAccount(accountService, accountId)) {
			accountService.depositMoney(accountService.findById(accountId), Double.valueOf(money));
			return accountService.findById(accountId);
		}
		return null;
	}

	@GetMapping("/withdraw")
	Account withdrawMoney (@PathVariable String id, @PathVariable String money) {
		Long accountId = Long.valueOf(id);
		if (validateAccount(accountService, accountId)) {
			accountService.withdrawMoney(accountService.findById(accountId), Double.valueOf(money));
			return accountService.findById(accountId);
		}
		return null;
	}

	@GetMapping("/transfer")
	boolean transferMoney (@PathVariable String sourceId, @PathVariable String destinationId, @PathVariable String money) {
		Long sourceAccountId = Long.valueOf(sourceId);
		Long destinationAccountId = Long.valueOf(destinationId);
		if (validateAccount(accountService, sourceAccountId) && validateAccount(accountService, destinationAccountId) ) {
			accountService.transferMoney(accountService.findById(sourceAccountId), accountService.findById(destinationAccountId), Double.valueOf(money));
			return true;
		}
		return false;
	}

}

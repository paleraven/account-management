package com.kretov.accountmanagement.controller;

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
@RequestMapping("/{customerId}/accounts")
public class AccountController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private CustomerService customerService;

	@GetMapping
	List<Account> readAccounts (@PathVariable String id){
		Long customerId = Long.valueOf(id);
		if (validateCustomer(accountService, customerId)) {
			return accountService.findByCustomer(customerService.findById(customerId));
		}
		return Collections.emptyList();
	}

}

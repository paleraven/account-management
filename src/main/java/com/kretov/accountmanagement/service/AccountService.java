package com.kretov.accountmanagement.service;

import com.kretov.accountmanagement.entity.Account;
import com.kretov.accountmanagement.entity.Customer;
import com.kretov.accountmanagement.repository.AccountRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
	@Autowired
	private AccountRepository accountRepository;

	public List<Account> findByCustomer(Customer customer) {
		return accountRepository.findByCustomer(customer);
	}

	public Account findById(Long id) {
		Optional<Account> customer = accountRepository.findById(id);
		return customer.orElseGet(null);
	}

	public Account save(Account account) {
		return accountRepository.save(account);
	}
}

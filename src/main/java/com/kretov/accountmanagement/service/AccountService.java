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
		Optional<Account> account = accountRepository.findById(id);
		return account.orElseGet(null);
	}

	public List<Account> findAllAccounts() {
		return accountRepository.findAll();
	}

	public Account save(Account account) {
		return accountRepository.save(account);
	}

	/**
	 * Положить деньги на счет
	 * @param account счет
	 * @param money сумма пополнения
	 */
	public void depositMoney(Account account, Double money) {
		account.setMoney(money);
		this.save(account);
	}

	/**
	 * Снять деньги со счета
	 * @param account счет
	 * @param money сумма снятия
	 * @return Выполнена ли операция (проверка на отрицательный баланс)
	 */
	public boolean withdrawMoney(Account account, Double money) {
		if (account.getMoney() - money < 0) {
			return false;
		} else {
			account.setMoney(account.getMoney() - money);
			this.save(account);
			return true;
		}
	}

	/**
	 * Перевести деньги со счета на счет
	 * @param sourceAccount Счет отправления
	 * @param destinationAccount Счет получения
	 * @param money Сумма перевода
	 * @return Выполнена ли операция (проверка на отрицательный баланс)
	 */
	public boolean transferMoney(Account sourceAccount, Account destinationAccount, Double money) {
		if (sourceAccount.getMoney() - money < 0) {
			return false;
		} else {
			sourceAccount.setMoney(sourceAccount.getMoney() - money);
			this.save(sourceAccount);
			destinationAccount.setMoney(destinationAccount.getMoney() + money);
			this.save(destinationAccount);
			return true;
		}
	}
}

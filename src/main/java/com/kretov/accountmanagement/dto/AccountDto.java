package com.kretov.accountmanagement.dto;

import com.kretov.accountmanagement.entity.Account;
import java.util.Optional;

public class AccountDto {
	private Long id;
	private String customerFirstName;
	private String customerLastName;
	private Double money;

	public AccountDto(Account account) {
		this.id = account.getId();
		this.customerFirstName = Optional.ofNullable(account.getCustomer().getFirstName()).orElse("");
		this.customerLastName = Optional.ofNullable(account.getCustomer().getLastName()).orElse("");
		this.money = account.getMoney();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomerFirstName() {
		return customerFirstName;
	}

	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}

	public String getCustomerLastName() {
		return customerLastName;
	}

	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	@Override
	public String toString() {
		return "id: " + this.id.toString() + "; Name: " + this.customerFirstName + "; Family: " + this.customerLastName + "; Money: " + this.money.toString();
	}

}

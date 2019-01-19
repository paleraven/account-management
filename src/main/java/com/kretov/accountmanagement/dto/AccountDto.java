package com.kretov.accountmanagement.dto;

import com.kretov.accountmanagement.entity.Account;

public class AccountDto {
	private Long customerId;
	private Double money;

	public AccountDto(Account account) {
		this.customerId = account.getCustomer().getId();
		this.money = account.getMoney();
	}

	public AccountDto() {
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

}

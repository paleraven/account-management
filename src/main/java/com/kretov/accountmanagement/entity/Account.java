package com.kretov.accountmanagement.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Entity
public class Account {
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@Column(name = "money")
	private Double money;

	public Account(Customer customer, Double money) {
		this.customer = customer;
		this.money = money;
	}

	public Account() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		if (this.id != null) {
			stringBuilder.append("Account id: ");
			stringBuilder.append(this.id.toString());
			stringBuilder.append(";");
		}
		if (this.customer != null) {
			if (!isBlank(this.getCustomer().getFirstName())) {
				stringBuilder.append(" Name: ");
				stringBuilder.append(this.getCustomer().getFirstName());
				stringBuilder.append(";");
			}
			if (!isBlank(this.getCustomer().getLastName())) {
				stringBuilder.append(" Family: ");
				stringBuilder.append(this.getCustomer().getLastName());
				stringBuilder.append(";");
			}
		}
		if (this.money != null) {
			stringBuilder.append(" Money: ");
			stringBuilder.append(this.money.toString());
		}
		return stringBuilder.toString();
	}

}

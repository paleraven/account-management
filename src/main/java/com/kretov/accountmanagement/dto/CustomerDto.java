package com.kretov.accountmanagement.dto;

import com.kretov.accountmanagement.entity.Customer;

public class CustomerDto {
	private String firstName;
	private String lastName;

	public CustomerDto(Customer customer) {
		this.firstName = customer.getFirstName();
		this.lastName = customer.getLastName();
	}

	public CustomerDto() {
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}

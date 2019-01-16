package com.kretov.accountmanagement.dto;

import com.kretov.accountmanagement.entity.Customer;

public class CustomerDto {
	private Long id;
	private String firstName;
	private String lastName;

	public CustomerDto(Customer customer) {
		this.id = customer.getId();
		this.firstName = customer.getFirstName();
		this.lastName = customer.getLastName();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	@Override
	public String toString() {
		return "id: " + this.id.toString() + " Name: " + this.firstName + " Family: " + this.lastName;
	}

}

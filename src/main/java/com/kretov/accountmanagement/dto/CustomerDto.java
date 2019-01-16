package com.kretov.accountmanagement.dto;

import com.kretov.accountmanagement.entity.Customer;

import static org.apache.commons.lang3.StringUtils.isBlank;

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
		StringBuilder stringBuilder = new StringBuilder();
		if (this.id != null) {
			stringBuilder.append("id: ");
			stringBuilder.append(this.id.toString());
			stringBuilder.append(";");
		}
		if (!isBlank(this.firstName)) {
			stringBuilder.append(" Name: ");
			stringBuilder.append(this.firstName);
			stringBuilder.append(";");
		}
		if (!isBlank(this.lastName)) {
			stringBuilder.append(" Family: ");
			stringBuilder.append(this.lastName);
		}
		return  stringBuilder.toString();
	}

}

package com.kretov.accountmanagement.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Entity
public class Customer {
	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

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
			stringBuilder.append("Id: ");
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

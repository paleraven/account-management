package com.kretov.accountmanagement.service;

import com.kretov.accountmanagement.entity.Customer;
import com.kretov.accountmanagement.repository.CustomerRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
	@Autowired
	private CustomerRepository customerRepository;

	public List<Customer> findAll() {
		return customerRepository.findAll();
	}

	public Customer findById(Long id) {
		Optional<Customer> customer = customerRepository.findById(id);
		return customer.orElse(null);
	}

	public Customer save(Customer customer) {
		return customerRepository.save(customer);
	}
}

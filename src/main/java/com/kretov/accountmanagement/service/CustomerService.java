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

	/**
	 * Получить всех клиентов
	 * @return список клиентов
	 */
	public List<Customer> findAll() {
		return customerRepository.findAll();
	}

	/**
	 * Найти конкретного клиента
	 * @param id идентификатор
	 * @return клиент
	 */
	public Customer findById(Long id) {
		Optional<Customer> customer = customerRepository.findById(id);
		return customer.orElse(null);
	}

	/**
	 * Сохранить клиента
	 * @param customer клиент
	 * @return актуальное состояние клиента
	 */
	public Customer save(Customer customer) {
		return customerRepository.save(customer);
	}

	/**
	 * Удалить клиента
	 * @param id идентификатор
	 */
	public void deleteById(Long id) {
		customerRepository.deleteById(id);
	}
}

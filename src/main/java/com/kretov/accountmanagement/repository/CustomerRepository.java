package com.kretov.accountmanagement.repository;

import com.kretov.accountmanagement.entity.Customer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	List<Customer> findAll();

	Optional<Customer> findById(Long id);

	Customer save(Customer customer);

}

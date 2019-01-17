package com.kretov.accountmanagement.repository;

import com.kretov.accountmanagement.entity.Account;
import com.kretov.accountmanagement.entity.Customer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

	List<Account> findByCustomer(Customer customer);

	Optional<Account> findById(Long id);

	Account save(Account account);

}

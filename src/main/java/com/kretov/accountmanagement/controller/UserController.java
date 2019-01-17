package com.kretov.accountmanagement.controller;

import com.kretov.accountmanagement.dto.CustomerDto;
import com.kretov.accountmanagement.entity.Customer;
import com.kretov.accountmanagement.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    private CustomerService customerService;

    /**
     * Получить всех клиентов
     * Пример запроса в curl:
     * curl localhost:9090/customers
     *
     * @return json со всеми клиентами
     */
    @GetMapping("/customers")
    List<String> getAllCustomers() {
        List<Customer> customers = customerService.findAll();
        return customers.stream().map(CustomerDto::new).map(CustomerDto::toString).collect(Collectors.toList());
    }
}

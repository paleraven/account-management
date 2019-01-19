package com.kretov.accountmanagement.controller;

import com.kretov.accountmanagement.dto.CustomerDto;
import com.kretov.accountmanagement.entity.Customer;
import com.kretov.accountmanagement.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bank")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * Получить всех клиентов
     *
     * @return json со всеми клиентами
     */
    @GetMapping("/customers")
    List<String> getAllCustomers() {
        List<Customer> customers = customerService.findAll();
        return customers.stream().map(CustomerDto::new).map(CustomerDto::toString).collect(Collectors.toList());
    }

    /**
     * Удалить клиента
     * @param id идентификатор
     * @return Статус операции
     */
    @DeleteMapping("/customerDelete/{id}")
    String deleteCustomerByCustomerId(@PathVariable String id) {
        Long customerId = Long.valueOf(id);
        Customer customer = customerService.findById(customerId);
        if (customer != null) {
            customerService.deleteById(customerId);
            return "Customer with id " + id + " was deleted";
        }
        return "Illegal customer id";
    }
}

package com.kretov.accountmanagement.controller;

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
        return customers.stream().map(Customer::toString).collect(Collectors.toList());
    }

    /**
     * Удалить клиента
     * @param id идентификатор
     * @return Статус операции
     */
    @DeleteMapping("/customerDelete/{id}")
    String deleteCustomerByCustomerId(@PathVariable String id) {
        try {
            Long customerId = Long.valueOf(id);
            Customer customer = customerService.findById(customerId);
            if (customer != null) {
                customerService.deleteById(customerId);
                return "Customer with id " + id + " was deleted";
            }
            return "Illegal customer id";
        } catch (NumberFormatException e) {
            return "Operation isn't executed. Illegal format of input data. Please, use number.";
        }
    }
}

package com.kretov.accountmanagement.controller;

import com.kretov.accountmanagement.dto.CustomerDto;
import com.kretov.accountmanagement.entity.Customer;
import com.kretov.accountmanagement.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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
            return "Operation isn't executed. Illegal customer id";
        } catch (NumberFormatException e) {
            return "Operation isn't executed. Illegal format of input data. Please, use number.";
        }
    }

    /**
     * Создать нового клиента
     * @param newCustomer dto с личными данными
     * @return Статус операции
     */
    @PostMapping(value="/customerCreate", consumes = APPLICATION_JSON_VALUE)
    String createCustomer(@RequestBody CustomerDto newCustomer) {
        Customer customer = new Customer();
        customer.setFirstName(newCustomer.getFirstName());
        customer.setLastName(newCustomer.getLastName());
        customerService.save(customer);
        return "Created customer with id " + customer.getId();
    }

    /**
     * Обновить существующего клиента
     * @param id идентификатор
     * @param updatedCustomer новые данные
     * @return Статус операции
     */
    @PutMapping(value="/customerUpdate/{id}", consumes = APPLICATION_JSON_VALUE)
    String updateCustomer(@PathVariable String id, @RequestBody CustomerDto updatedCustomer) {
        try {
            Long customerId = Long.valueOf(id);
            Customer customer = customerService.findById(customerId);
            if (customer != null) {
                customer.setFirstName(updatedCustomer.getFirstName());
                customer.setLastName(updatedCustomer.getLastName());
                customerService.save(customer);
                return "Updated customer with id " + customer.getId();
            }
            return "Operation isn't executed. Illegal customer id";
        } catch (NumberFormatException e) {
            return "Operation isn't executed. Illegal format of input data. Please, use number.";
        }
    }
}

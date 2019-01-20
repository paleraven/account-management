package com.kretov.accountmanagement.rest;

import com.kretov.accountmanagement.controller.CustomerController;
import com.kretov.accountmanagement.dto.CustomerDto;
import com.kretov.accountmanagement.entity.Customer;
import com.kretov.accountmanagement.response.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(value = "Customer", description = "API для работы с клиентами")
@RestController
@RequestMapping("/bank")
public class CustomerRestController {

    @Autowired
    private CustomerController customerController;

    /**
     * Получить всех клиентов
     *
     * @return список клиентов
     */
    @ApiOperation(value = "Получить всех клиентов")
    @GetMapping("/customers")
    List<String> getAllCustomers() {
        Response<Customer> response = customerController.getAllCustomers();
        return response.getResult().stream().map(Customer::toString).collect(Collectors.toList());
    }

    /**
     * Удалить клиента
     * @param id идентификатор
     * @return Статус операции
     */
    @ApiOperation(value = "Удалить клиента")
    @DeleteMapping("/customerDelete/{id}")
    String deleteCustomerByCustomerId(@PathVariable String id) {
        Response<Customer> response = customerController.deleteCustomerByCustomerId(id);
        return response.getDescription();
    }

    /**
     * Создать нового клиента
     * @param newCustomer dto с личными данными
     * @return Статус операции
     */
    @ApiOperation(value = "Создать клиента")
    @PostMapping(value="/customerCreate", consumes = APPLICATION_JSON_VALUE)
    String createCustomer(@RequestBody CustomerDto newCustomer) {
        Customer customer = new Customer();
        customer.setFirstName(newCustomer.getFirstName());
        customer.setLastName(newCustomer.getLastName());
        Response<Customer> response = customerController.createCustomer(customer);
        return response.getDescription();
    }

    /**
     * Обновить существующего клиента
     * @param id идентификатор
     * @param updatedCustomer новые данные
     * @return Статус операции
     */
    @ApiOperation(value = "Изменить данные клиента")
    @PutMapping(value="/customerUpdate/{id}", consumes = APPLICATION_JSON_VALUE)
    String updateCustomer(@PathVariable String id, @RequestBody CustomerDto updatedCustomer) {
        try {
            Customer customer = new Customer();
            customer.setId(Long.valueOf(id));
            customer.setFirstName(updatedCustomer.getFirstName());
            customer.setLastName(updatedCustomer.getLastName());
            Response<Customer> response = customerController.updateCustomer(customer);
            return response.getDescription();
        } catch (NumberFormatException e) {
            return "Illegal format of input data. Please, use number.";
        }
    }
}

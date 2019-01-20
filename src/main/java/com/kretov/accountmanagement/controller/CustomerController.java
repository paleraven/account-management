package com.kretov.accountmanagement.controller;

import com.kretov.accountmanagement.entity.Customer;
import com.kretov.accountmanagement.response.Response;
import com.kretov.accountmanagement.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.List;

import static com.kretov.accountmanagement.response.Response.illegalFormatResponse;
import static com.kretov.accountmanagement.response.Status.ERROR;
import static com.kretov.accountmanagement.response.Status.SUCCESS;

@Controller
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    /**
     * Получить всех клиентов
     *
     * @return Response<Customer>
     */
    public Response<Customer> getAllCustomers() {
        List<Customer> customers = customerService.findAll();
        if (customers.isEmpty()) {
            return new Response<>(ERROR,"No customers", Collections.emptyList());
        }
        return new Response<>(SUCCESS,"Found " + customers.size() + " customers", customers);
    }

    /**
     * Получить клиента по id
     * @param id идентификатор
     * @return Response<Customer>
     */
    public Response<Customer> findById(String id) {
        try {
            Long customerId = Long.valueOf(id);
            Customer customer = customerService.findById(customerId);
            if (customer != null) {
                return new Response<>(SUCCESS, "Customer with id " + id + " was found", Collections.singletonList(customer));
            }
            return new Response<>(ERROR, "No customers", Collections.emptyList());
        } catch (NumberFormatException e) {
            return illegalFormatResponse();
        }
    }

    /**
     * Получить клиентов по фамилии
     * @param lastName фамилия
     * @return Response<Customer>
     */
    public Response<Customer> findByLastName(String lastName) {
        List<Customer> customers = customerService.findByLastName(lastName);
        if (customers.isEmpty()) {
            return new Response<>(ERROR,"No customers with family " + lastName, Collections.emptyList());
        }
        return new Response<>(SUCCESS,"Found " + customers.size() + " customers", customers);
    }

    /**
     * Удалить клиента
     * @param id идентификатор
     * @return Response<Customer>
     */
    public Response<Customer> deleteCustomerByCustomerId(String id) {
        try {
            Long customerId = Long.valueOf(id);
            Customer customer = customerService.findById(customerId);
            if (customer != null) {
                customerService.deleteById(customerId);
                return new Response<>(SUCCESS,"Customer with id " + id + " was deleted", Collections.emptyList());
            }
            return new Response<>(ERROR,"Illegal customer id", Collections.emptyList());
        } catch (NumberFormatException e) {
            return illegalFormatResponse();
        }
    }

    /**
     * Создать нового клиента
     * @param newCustomer dto с личными данными
     * @return Response<Customer>
     */
    public Response<Customer> createCustomer(Customer newCustomer) {
        try {
            customerService.save(newCustomer);
            return new Response<>(SUCCESS, "Created customer with id " + newCustomer.getId(), Collections.singletonList(newCustomer));
        } catch (Exception e) {
            return new Response<>(ERROR,"Customer wasn't created", Collections.emptyList());
        }
    }

    /**
     * Обновить клиента
     * @param customer клиент
     * @return Response<Customer>
     */
    public Response<Customer> updateCustomer(Customer customer) {
        try {
            Customer existCustomer = customerService.findById(customer.getId());
            if (existCustomer != null) {
                existCustomer.setLastName(customer.getLastName());
                existCustomer.setFirstName(customer.getFirstName());
                customerService.save(existCustomer);
                return new Response<>(SUCCESS, "Updated customer with id " + customer.getId(), Collections.singletonList(existCustomer));
            }
            return new Response<>(ERROR,"Customer doesn't exist", Collections.emptyList());
        } catch (Exception e) {
            return new Response<>(ERROR,"Customer wasn't updated", Collections.emptyList());
        }
    }
}

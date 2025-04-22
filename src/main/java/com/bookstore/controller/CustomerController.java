package com.bookstore.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.model.Customer;
import com.bookstore.service.CustomerService;

import java.util.List;

import lombok.RequiredArgsConstructor;

/**
 * Controller for managing customers and their loyalty points
 */
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    /**
     * Get all customers
     *
     * @return List of all customers
     */
    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    /**
     * Get a customer by ID
     *
     * @param id The customer ID
     * @return The customer, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable(name = "id") Long id) {
        return customerService.getCustomerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Add a new customer
     *
     * @param customer The customer to add
     * @return The added customer
     */
    @PostMapping
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.addCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
    }

    /**
     * Update an existing customer
     *
     * @param id       The customer ID
     * @param customer The updated customer details
     * @return The updated customer, or 404 if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable(name = "id") Long id, @RequestBody Customer customer) {
        return customerService.updateCustomer(id, customer)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get a customer's loyalty points
     *
     * @param id The customer ID
     * @return The loyalty points, or 404 if the customer is not found
     */
    @GetMapping("/{id}/loyalty-points")
    public ResponseEntity<Integer> getLoyaltyPoints(@PathVariable(name = "id") Long id) {
        return customerService.getCustomerById(id)
                .map(c -> ResponseEntity.ok(c.getLoyaltyPoints()))
                .orElse(ResponseEntity.notFound().build());
    }
}
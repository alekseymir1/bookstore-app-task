package com.bookstore.service;

import org.springframework.stereotype.Service;

import com.bookstore.model.Customer;
import com.bookstore.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

/**
 * Service for managing customers and their loyalty points
 */
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    /**
     * Customer Management Methods
     */
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public Customer addCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Optional<Customer> updateCustomer(Long id, Customer customerDetails) {
        return customerRepository.findById(id)
                .map(existingCustomer -> {
                    existingCustomer.setName(customerDetails.getName());
                    existingCustomer.setLoyaltyPoints(customerDetails.getLoyaltyPoints());
                    return customerRepository.save(existingCustomer);
                });
    }

    /**
     * Loyalty Points Methods
     */
    public int calculateLoyaltyPoints(int currentPoints, int booksPurchased) {
        // Add 1 point per book purchased
        int updated = currentPoints + booksPurchased;

        // If 10 or more points accumulated and used for a free book, reset to 0
        return updated >= 10 ? 0 : updated;
    }

    /**
     * Update customer loyalty points after a purchase
     *
     * @param customerId     The ID of the customer
     * @param booksPurchased The number of books purchased
     * @return The updated customer, or empty if the customer doesn't exist
     */
    public Optional<Customer> updateLoyaltyPoints(Long customerId, int booksPurchased) {
        return customerRepository.findById(customerId)
                .map(customer -> {
                    int newPoints = calculateLoyaltyPoints(customer.getLoyaltyPoints(), booksPurchased);
                    customer.setLoyaltyPoints(newPoints);
                    return customerRepository.save(customer);
                });
    }
}

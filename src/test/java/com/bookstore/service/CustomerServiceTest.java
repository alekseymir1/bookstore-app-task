package com.bookstore.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bookstore.model.Customer;
import com.bookstore.repository.CustomerRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer(1L, "Test Customer", 5);
    }

    @Test
    void getAllCustomers_ShouldReturnAllCustomers() {
        // Arrange
        List<Customer> expectedCustomers = Arrays.asList(
                testCustomer,
                new Customer(2L, "Another Customer", 10)
        );
        when(customerRepository.findAll()).thenReturn(expectedCustomers);

        // Act
        List<Customer> actualCustomers = customerService.getAllCustomers();

        // Assert
        assertEquals(expectedCustomers.size(), actualCustomers.size());
        assertEquals(expectedCustomers, actualCustomers);
        verify(customerRepository).findAll();
    }

    @Test
    void getCustomerById_WithExistingId_ShouldReturnCustomer() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));

        // Act
        Optional<Customer> result = customerService.getCustomerById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testCustomer, result.get());
        verify(customerRepository).findById(1L);
    }

    @Test
    void getCustomerById_WithNonExistingId_ShouldReturnEmpty() {
        // Arrange
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Customer> result = customerService.getCustomerById(99L);

        // Assert
        assertFalse(result.isPresent());
        verify(customerRepository).findById(99L);
    }

    @Test
    void addCustomer_ShouldSaveAndReturnCustomer() {
        // Arrange
        Customer newCustomer = new Customer(null, "New Customer", 0);
        Customer savedCustomer = new Customer(3L, "New Customer", 0);
        when(customerRepository.save(newCustomer)).thenReturn(savedCustomer);

        // Act
        Customer result = customerService.addCustomer(newCustomer);

        // Assert
        assertEquals(savedCustomer, result);
        verify(customerRepository).save(newCustomer);
    }

    @Test
    void updateCustomer_WithExistingId_ShouldUpdateAndReturnCustomer() {
        // Arrange
        Customer updatedDetails = new Customer(1L, "Updated Customer", 15);
        Customer existingCustomer = testCustomer;
        Customer savedCustomer = new Customer(1L, "Updated Customer", 15);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        // Act
        Optional<Customer> result = customerService.updateCustomer(1L, updatedDetails);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Updated Customer", result.get().getName());
        assertEquals(15, result.get().getLoyaltyPoints());
        verify(customerRepository).findById(1L);
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void updateCustomer_WithNonExistingId_ShouldReturnEmpty() {
        // Arrange
        Customer updatedDetails = new Customer(99L, "Updated Customer", 15);
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Customer> result = customerService.updateCustomer(99L, updatedDetails);

        // Assert
        assertFalse(result.isPresent());
        verify(customerRepository).findById(99L);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void calculateLoyaltyPoints_WithLessThan10Points_ShouldAddPoints() {
        // Act
        int result = customerService.calculateLoyaltyPoints(5, 3);

        // Assert
        assertEquals(8, result);
    }

    @Test
    void calculateLoyaltyPoints_WithExactly10Points_ShouldResetToZero() {
        // Act
        int result = customerService.calculateLoyaltyPoints(7, 3);

        // Assert
        assertEquals(0, result);
    }

    @Test
    void calculateLoyaltyPoints_WithMoreThan10Points_ShouldResetToZero() {
        // Act
        int result = customerService.calculateLoyaltyPoints(8, 3);

        // Assert
        assertEquals(0, result);
    }

    @Test
    void updateLoyaltyPoints_WithExistingCustomer_ShouldUpdatePoints() {
        // Arrange
        Customer existingCustomer = testCustomer;
        Customer updatedCustomer = new Customer(1L, "Test Customer", 8);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

        // Act
        Optional<Customer> result = customerService.updateLoyaltyPoints(1L, 3);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(8, result.get().getLoyaltyPoints());
        verify(customerRepository).findById(1L);
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void updateLoyaltyPoints_WithNonExistingCustomer_ShouldReturnEmpty() {
        // Arrange
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Customer> result = customerService.updateLoyaltyPoints(99L, 3);

        // Assert
        assertFalse(result.isPresent());
        verify(customerRepository).findById(99L);
        verify(customerRepository, never()).save(any(Customer.class));
    }
}
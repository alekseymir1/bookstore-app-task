package com.bookstore.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bookstore.model.Customer;
import com.bookstore.service.CustomerService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

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
        when(customerService.getAllCustomers()).thenReturn(expectedCustomers);

        // Act
        List<Customer> actualCustomers = customerController.getAllCustomers();

        // Assert
        assertEquals(expectedCustomers.size(), actualCustomers.size());
        assertEquals(expectedCustomers, actualCustomers);
        verify(customerService).getAllCustomers();
    }

    @Test
    void getCustomerById_WithExistingId_ShouldReturnCustomer() {
        // Arrange
        when(customerService.getCustomerById(1L)).thenReturn(Optional.of(testCustomer));

        // Act
        ResponseEntity<Customer> response = customerController.getCustomerById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testCustomer, response.getBody());
        verify(customerService).getCustomerById(1L);
    }

    @Test
    void getCustomerById_WithNonExistingId_ShouldReturnNotFound() {
        // Arrange
        when(customerService.getCustomerById(99L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Customer> response = customerController.getCustomerById(99L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(customerService).getCustomerById(99L);
    }

    @Test
    void addCustomer_ShouldReturnCreatedCustomer() {
        // Arrange
        Customer newCustomer = new Customer(null, "New Customer", 0);
        Customer savedCustomer = new Customer(3L, "New Customer", 0);
        when(customerService.addCustomer(newCustomer)).thenReturn(savedCustomer);

        // Act
        ResponseEntity<Customer> response = customerController.addCustomer(newCustomer);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedCustomer, response.getBody());
        verify(customerService).addCustomer(newCustomer);
    }

    @Test
    void updateCustomer_WithExistingId_ShouldReturnUpdatedCustomer() {
        // Arrange
        Customer updatedDetails = new Customer(1L, "Updated Customer", 15);
        when(customerService.updateCustomer(1L, updatedDetails)).thenReturn(Optional.of(updatedDetails));

        // Act
        ResponseEntity<Customer> response = customerController.updateCustomer(1L, updatedDetails);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedDetails, response.getBody());
        verify(customerService).updateCustomer(1L, updatedDetails);
    }

    @Test
    void updateCustomer_WithNonExistingId_ShouldReturnNotFound() {
        // Arrange
        Customer updatedDetails = new Customer(99L, "Updated Customer", 15);
        when(customerService.updateCustomer(99L, updatedDetails)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Customer> response = customerController.updateCustomer(99L, updatedDetails);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(customerService).updateCustomer(99L, updatedDetails);
    }

    @Test
    void getLoyaltyPoints_WithExistingId_ShouldReturnPoints() {
        // Arrange
        when(customerService.getCustomerById(1L)).thenReturn(Optional.of(testCustomer));

        // Act
        ResponseEntity<Integer> response = customerController.getLoyaltyPoints(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5, response.getBody());
        verify(customerService).getCustomerById(1L);
    }

    @Test
    void getLoyaltyPoints_WithNonExistingId_ShouldReturnNotFound() {
        // Arrange
        when(customerService.getCustomerById(99L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Integer> response = customerController.getLoyaltyPoints(99L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(customerService).getCustomerById(99L);
    }
}
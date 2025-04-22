package com.bookstore.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bookstore.dto.PurchaseRequest;
import com.bookstore.dto.PurchaseResponse;
import com.bookstore.model.Book;
import com.bookstore.model.BookType;
import com.bookstore.model.Customer;
import com.bookstore.service.BookService;
import com.bookstore.service.CustomerService;
import com.bookstore.service.PurchaseService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PurchaseControllerTest {

    @Mock
    private BookService bookService;

    @Mock
    private CustomerService customerService;

    @Mock
    private PurchaseService purchaseService;

    @InjectMocks
    private PurchaseController purchaseController;

    private Customer customer;
    private Book book1;
    private Book book2;
    private PurchaseRequest purchaseRequest;
    private PurchaseResponse purchaseResponse;

    @BeforeEach
    void setUp() {
        customer = new Customer(1L, "Test Customer", 5);
        book1 = new Book(1L, "Book 1", new BigDecimal("29.99"), BookType.REGULAR);
        book2 = new Book(2L, "Book 2", new BigDecimal("19.99"), BookType.OLD_EDITION);

        purchaseRequest = new PurchaseRequest();
        purchaseRequest.setCustomerId(1L);
        purchaseRequest.setBookIds(Arrays.asList(1L, 2L));

        purchaseResponse = new PurchaseResponse();
        purchaseResponse.setTotalPrice(new BigDecimal("45.98"));
        purchaseResponse.setLoyaltyPointsEarned(7);
        purchaseResponse.setBooks(Arrays.asList(book1, book2));
        purchaseResponse.setFreeBooks(new ArrayList<>());
    }

    @Test
    void purchaseBooks_WithValidRequest_ShouldReturnPurchaseResponse() {
        // Arrange
        when(customerService.getCustomerById(1L)).thenReturn(Optional.of(customer));
        when(bookService.getBookById(1L)).thenReturn(Optional.of(book1));
        when(bookService.getBookById(2L)).thenReturn(Optional.of(book2));
        when(purchaseService.purchase(any(Customer.class), anyList())).thenReturn(purchaseResponse);

        // Act
        ResponseEntity<PurchaseResponse> response = purchaseController.purchaseBooks(purchaseRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(purchaseResponse, response.getBody());
        verify(customerService).getCustomerById(1L);
        verify(bookService).getBookById(1L);
        verify(bookService).getBookById(2L);
        verify(purchaseService).purchase(any(Customer.class), anyList());
    }

    @Test
    void purchaseBooks_WithNonExistingCustomer_ShouldThrowException() {
        // Arrange
        when(customerService.getCustomerById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            purchaseController.purchaseBooks(purchaseRequest);
        });

        verify(customerService).getCustomerById(1L);
        verify(bookService, never()).getBookById(anyLong());
        verify(purchaseService, never()).purchase(any(Customer.class), anyList());
    }

    @Test
    void purchaseBooks_WithNonExistingBook_ShouldThrowException() {
        // Arrange
        when(customerService.getCustomerById(1L)).thenReturn(Optional.of(customer));
        when(bookService.getBookById(1L)).thenReturn(Optional.of(book1));
        when(bookService.getBookById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            purchaseController.purchaseBooks(purchaseRequest);
        });

        verify(customerService).getCustomerById(1L);
        verify(bookService).getBookById(1L);
        verify(bookService).getBookById(2L);
        verify(purchaseService, never()).purchase(any(Customer.class), anyList());
    }
}
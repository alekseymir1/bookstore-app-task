package com.bookstore.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bookstore.dto.PricingRequest;
import com.bookstore.dto.PricingResponse;
import com.bookstore.model.Book;
import com.bookstore.model.BookType;
import com.bookstore.model.Customer;
import com.bookstore.service.BookService;
import com.bookstore.service.CustomerService;
import com.bookstore.service.pricing.PricingService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PricingControllerTest {

    @Mock
    private BookService bookService;

    @Mock
    private CustomerService customerService;

    @Mock
    private PricingService pricingService;

    @InjectMocks
    private PricingController pricingController;

    private Customer customer;
    private Book book1;
    private Book book2;
    private PricingRequest pricingRequest;
    private PricingResponse pricingResponse;

    @BeforeEach
    void setUp() {
        customer = new Customer(1L, "Test Customer", 5);
        book1 = new Book(1L, "Book 1", new BigDecimal("29.99"), BookType.REGULAR);
        book2 = new Book(2L, "Book 2", new BigDecimal("19.99"), BookType.OLD_EDITION);

        pricingRequest = new PricingRequest();
        pricingRequest.setCustomerId(1L);
        pricingRequest.setBookIds(Arrays.asList(1L, 2L));

        pricingResponse = new PricingResponse();
        pricingResponse.setTotalPrice(new BigDecimal("45.98"));
        pricingResponse.setBooks(Arrays.asList(book1, book2));
        pricingResponse.setFreeBooks(new ArrayList<>());
    }

    @Test
    void calculatePrice_WithValidRequest_ShouldReturnPricingResponse() {
        // Arrange
        when(customerService.getCustomerById(1L)).thenReturn(Optional.of(customer));
        when(bookService.getBookById(1L)).thenReturn(Optional.of(book1));
        when(bookService.getBookById(2L)).thenReturn(Optional.of(book2));
        when(pricingService.calculatePrice(anyList(), anyInt(), anyList())).thenReturn(new BigDecimal("45.98"));

        // Act
        ResponseEntity<PricingResponse> response = pricingController.calculatePrice(pricingRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new BigDecimal("45.98"), response.getBody().getTotalPrice());
        assertEquals(2, response.getBody().getBooks().size());
        assertEquals(0, response.getBody().getFreeBooks().size());
        verify(customerService).getCustomerById(1L);
        verify(bookService).getBookById(1L);
        verify(bookService).getBookById(2L);
        verify(pricingService).calculatePrice(anyList(), anyInt(), anyList());
    }

    @Test
    void calculatePrice_WithNonExistingCustomer_ShouldThrowException() {
        // Arrange
        when(customerService.getCustomerById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            pricingController.calculatePrice(pricingRequest);
        });

        verify(customerService).getCustomerById(1L);
        verify(bookService, never()).getBookById(anyLong());
        verify(pricingService, never()).calculatePrice(anyList(), anyInt(), anyList());
    }

    @Test
    void calculatePrice_WithNonExistingBook_ShouldThrowException() {
        // Arrange
        when(customerService.getCustomerById(1L)).thenReturn(Optional.of(customer));
        when(bookService.getBookById(1L)).thenReturn(Optional.of(book1));
        when(bookService.getBookById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            pricingController.calculatePrice(pricingRequest);
        });

        verify(customerService).getCustomerById(1L);
        verify(bookService).getBookById(1L);
        verify(bookService).getBookById(2L);
        verify(pricingService, never()).calculatePrice(anyList(), anyInt(), anyList());
    }
}
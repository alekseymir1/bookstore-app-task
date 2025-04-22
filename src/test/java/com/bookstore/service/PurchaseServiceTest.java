package com.bookstore.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bookstore.dto.PurchaseResponse;
import com.bookstore.model.Book;
import com.bookstore.model.BookType;
import com.bookstore.model.Customer;
import com.bookstore.service.pricing.PricingService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PurchaseServiceTest {

    @Mock
    private PricingService pricingService;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private PurchaseService purchaseService;

    private Customer customer;
    private Book book1;
    private Book book2;
    private List<Book> books;

    @BeforeEach
    void setUp() {
        customer = new Customer(1L, "Test Customer", 5);
        book1 = new Book(1L, "Book 1", new BigDecimal("29.99"), BookType.REGULAR);
        book2 = new Book(2L, "Book 2", new BigDecimal("19.99"), BookType.OLD_EDITION);
        books = Arrays.asList(book1, book2);
    }

    @Test
    void purchase_WithNoFreeBooks_ShouldCalculatePriceAndReturnResponse() {
        // Arrange
        BigDecimal totalPrice = new BigDecimal("45.98");

        when(pricingService.calculatePrice(eq(books), eq(5), any())).thenReturn(totalPrice);

        // Act
        PurchaseResponse response = purchaseService.purchase(customer, books);

        // Assert
        assertEquals(totalPrice, response.getTotalPrice());
        assertEquals(5, response.getLoyaltyPointsEarned());
        assertEquals(books, response.getBooks());
        assertTrue(response.getFreeBooks().isEmpty());

        verify(pricingService).calculatePrice(eq(books), eq(5), any());
        verify(customerService, never()).updateCustomer(anyLong(), any());
    }

    @Test
    void purchase_WithFreeBook_ShouldResetLoyaltyPointsAndReturnResponse() {
        // Arrange
        BigDecimal totalPrice = new BigDecimal("29.99");

        when(pricingService.calculatePrice(eq(books), eq(5), any())).thenAnswer(invocation -> {
            List<Book> freeBooksOut = invocation.getArgument(2);
            freeBooksOut.add(book2);
            return totalPrice;
        });

        when(customerService.updateCustomer(eq(1L), any())).thenReturn(Optional.of(new Customer(1L, "Test Customer", 0)));

        // Act
        PurchaseResponse response = purchaseService.purchase(customer, books);

        // Assert
        assertEquals(totalPrice, response.getTotalPrice());
        assertEquals(0, response.getLoyaltyPointsEarned());
        assertEquals(books, response.getBooks());
        assertEquals(1, response.getFreeBooks().size());
        assertEquals(book2, response.getFreeBooks().get(0));

        verify(pricingService).calculatePrice(eq(books), eq(5), any());
        verify(customerService).updateCustomer(eq(1L), any());
    }
}
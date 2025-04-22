package com.bookstore.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bookstore.model.Book;
import com.bookstore.model.BookType;
import com.bookstore.model.Customer;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class LoyaltyServiceTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private LoyaltyService loyaltyService;

    private Book newReleaseBook;
    private Book regularBook;
    private Book oldEditionBook;
    private Customer customer;

    @BeforeEach
    void setUp() {
        newReleaseBook = new Book(1L, "New Release", new BigDecimal("39.99"), BookType.NEW_RELEASE);
        regularBook = new Book(2L, "Regular Book", new BigDecimal("29.99"), BookType.REGULAR);
        oldEditionBook = new Book(3L, "Old Edition", new BigDecimal("19.99"), BookType.OLD_EDITION);
        customer = new Customer(1L, "Test Customer", 0);
    }

    @Test
    void isEligibleForFreeRedemption_WithNewRelease_ShouldReturnFalse() {
        // Act
        boolean result = loyaltyService.isEligibleForFreeRedemption(newReleaseBook);

        // Assert
        assertFalse(result);
    }

    @Test
    void isEligibleForFreeRedemption_WithRegularBook_ShouldReturnTrue() {
        // Act
        boolean result = loyaltyService.isEligibleForFreeRedemption(regularBook);

        // Assert
        assertTrue(result);
    }

    @Test
    void isEligibleForFreeRedemption_WithOldEditionBook_ShouldReturnTrue() {
        // Act
        boolean result = loyaltyService.isEligibleForFreeRedemption(oldEditionBook);

        // Assert
        assertTrue(result);
    }

    @Test
    void hasEnoughPointsForFreeBook_WithLessThan10Points_ShouldReturnFalse() {
        // Arrange
        int loyaltyPoints = 9;

        // Act
        boolean result = loyaltyService.hasEnoughPointsForFreeBook(loyaltyPoints);

        // Assert
        assertFalse(result);
    }

    @Test
    void hasEnoughPointsForFreeBook_With10Points_ShouldReturnTrue() {
        // Arrange
        int loyaltyPoints = 10;

        // Act
        boolean result = loyaltyService.hasEnoughPointsForFreeBook(loyaltyPoints);

        // Assert
        assertTrue(result);
    }

    @Test
    void hasEnoughPointsForFreeBook_WithMoreThan10Points_ShouldReturnTrue() {
        // Arrange
        int loyaltyPoints = 15;

        // Act
        boolean result = loyaltyService.hasEnoughPointsForFreeBook(loyaltyPoints);

        // Assert
        assertTrue(result);
    }
}
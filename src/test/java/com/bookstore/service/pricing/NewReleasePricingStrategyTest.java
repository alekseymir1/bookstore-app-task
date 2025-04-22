package com.bookstore.service.pricing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bookstore.model.Book;
import com.bookstore.model.BookType;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NewReleasePricingStrategyTest {

    private NewReleasePricingStrategy strategy;
    private Book newReleaseBook;

    @BeforeEach
    void setUp() {
        strategy = new NewReleasePricingStrategy();
        newReleaseBook = new Book(1L, "New Release", new BigDecimal("39.99"), BookType.NEW_RELEASE);
    }

    @Test
    void calculatePrice_ShouldReturnFullPrice() {
        // Act
        BigDecimal price = strategy.calculatePrice(newReleaseBook, false);

        // Assert
        assertEquals(newReleaseBook.getBasePrice(), price);
    }

    @Test
    void calculatePrice_WithBundle_ShouldStillReturnFullPrice() {
        // Act
        BigDecimal price = strategy.calculatePrice(newReleaseBook, true);

        // Assert
        assertEquals(newReleaseBook.getBasePrice(), price);
    }

    @Test
    void getType_ShouldReturnNewRelease() {
        // Act
        BookType type = strategy.getType();

        // Assert
        assertEquals(BookType.NEW_RELEASE, type);
    }
}
package com.bookstore.service.pricing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bookstore.model.Book;
import com.bookstore.model.BookType;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegularPricingStrategyTest {

    private RegularPricingStrategy strategy;
    private Book regularBook;

    @BeforeEach
    void setUp() {
        strategy = new RegularPricingStrategy();
        regularBook = new Book(1L, "Regular Book", new BigDecimal("29.99"), BookType.REGULAR);
    }

    @Test
    void calculatePrice_WithoutBundle_ShouldReturnFullPrice() {
        // Act
        BigDecimal price = strategy.calculatePrice(regularBook, false);

        // Assert
        assertEquals(regularBook.getBasePrice().setScale(2, RoundingMode.HALF_UP), price);
    }

    @Test
    void calculatePrice_WithBundle_ShouldApply10PercentDiscount() {
        // Act
        BigDecimal price = strategy.calculatePrice(regularBook, true);

        // Assert
        // Expected: 29.99 * 0.9 = 26.99
        BigDecimal expected = regularBook.getBasePrice()
                .multiply(new BigDecimal("0.9"))
                .setScale(2, RoundingMode.HALF_UP);
        assertEquals(expected, price);
    }

    @Test
    void getType_ShouldReturnRegular() {
        // Act
        BookType type = strategy.getType();

        // Assert
        assertEquals(BookType.REGULAR, type);
    }
}
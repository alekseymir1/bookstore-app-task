package com.bookstore.service.pricing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bookstore.model.Book;
import com.bookstore.model.BookType;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OldEditionPricingStrategyTest {

    private OldEditionPricingStrategy strategy;
    private Book oldEditionBook;

    @BeforeEach
    void setUp() {
        strategy = new OldEditionPricingStrategy();
        oldEditionBook = new Book(1L, "Old Edition", new BigDecimal("19.99"), BookType.OLD_EDITION);
    }

    @Test
    void calculatePrice_WithoutBundle_ShouldApply20PercentDiscount() {
        // Act
        BigDecimal price = strategy.calculatePrice(oldEditionBook, false);

        // Assert
        // Expected: 19.99 * 0.8 = 15.99
        BigDecimal expected = oldEditionBook.getBasePrice()
                .multiply(new BigDecimal("0.8"))
                .setScale(2, RoundingMode.HALF_UP);
        assertEquals(expected, price);
    }

    @Test
    void calculatePrice_WithBundle_ShouldApply20PercentAnd5PercentDiscount() {
        // Act
        BigDecimal price = strategy.calculatePrice(oldEditionBook, true);

        // Assert
        // Expected: 19.99 * 0.8 * 0.95 = 15.19
        BigDecimal expected = oldEditionBook.getBasePrice()
                .multiply(new BigDecimal("0.8"))
                .multiply(new BigDecimal("0.95"))
                .setScale(2, RoundingMode.HALF_UP);
        assertEquals(expected, price);
    }

    @Test
    void getType_ShouldReturnOldEdition() {
        // Act
        BookType type = strategy.getType();

        // Assert
        assertEquals(BookType.OLD_EDITION, type);
    }
}
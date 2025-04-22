package com.bookstore.service.pricing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bookstore.model.Book;
import com.bookstore.model.BookType;
import com.bookstore.service.LoyaltyService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PricingServiceTest {


    private PricingService pricingService;

    private Book newReleaseBook;
    private Book regularBook;
    private Book oldEditionBook;


    @BeforeEach
    void setUp() {
        pricingService = new PricingService(
                new PricingStrategyFactory(
                        List.of(new OldEditionPricingStrategy(), new NewReleasePricingStrategy(), new RegularPricingStrategy())),
                new LoyaltyService());
        newReleaseBook = new Book(1L, "New Release", new BigDecimal("39.99"), BookType.NEW_RELEASE);
        regularBook = new Book(2L, "Regular Book", new BigDecimal("29.99"), BookType.REGULAR);
        oldEditionBook = new Book(3L, "Old Edition", new BigDecimal("19.99"), BookType.OLD_EDITION);
    }

    @Test
    void calculateBookPrice_ShouldUseCorrectStrategy() {
        // Act
        BigDecimal newReleasePrice = pricingService.calculateBookPrice(newReleaseBook, false);
        BigDecimal regularPrice = pricingService.calculateBookPrice(regularBook, false);
        BigDecimal oldEditionPrice = pricingService.calculateBookPrice(oldEditionBook, false);

        // Assert
        assertEquals(new BigDecimal("39.99"), newReleasePrice);
        assertEquals(new BigDecimal("29.99"), regularPrice);
        assertEquals(new BigDecimal("15.99"), oldEditionPrice);

    }

    @Test
    void calculateBookPrice_WithBundle_ShouldApplyBundleDiscount() {
        // Act
        BigDecimal newReleasePrice = pricingService.calculateBookPrice(newReleaseBook, true);
        BigDecimal regularPrice = pricingService.calculateBookPrice(regularBook, true);
        BigDecimal oldEditionPrice = pricingService.calculateBookPrice(oldEditionBook, true);

        // Assert
        assertEquals(new BigDecimal("39.99"), newReleasePrice); // No discount for new releases
        assertEquals(new BigDecimal("26.99"), regularPrice); // 10% discount
        assertEquals(new BigDecimal("15.19"), oldEditionPrice); // Additional 5% discount
    }

    @Test
    void calculatePrice_WithNoFreeBooks_ShouldCalculateTotalPrice() {
        // Arrange
        List<Book> books = Arrays.asList(newReleaseBook, regularBook, oldEditionBook);
        List<Book> freeBooks = new ArrayList<>();

        // Act
        BigDecimal totalPrice = pricingService.calculatePrice(books, 5, freeBooks);

        // Assert
        // Expected: 39.99 (new release) + 26.99 (regular with bundle) + 15.19 (old edition with bundle) = 82.17
        assertEquals(new BigDecimal("82.17").setScale(2, RoundingMode.HALF_UP), totalPrice);
        assertTrue(freeBooks.isEmpty());
    }

    @Test
    void calculatePrice_WithFreeEligibleBook_ShouldAddToFreeBooks() {
        // Arrange
        List<Book> books = Arrays.asList(regularBook, oldEditionBook);
        List<Book> freeBooks = new ArrayList<>();
        // Act
        BigDecimal totalPrice = pricingService.calculatePrice(books, 10, freeBooks);

        // Assert
        assertEquals(new BigDecimal("29.99").setScale(2, RoundingMode.HALF_UP), totalPrice);
        assertEquals(1, freeBooks.size());
        assertEquals(oldEditionBook, freeBooks.getFirst());
    }

    @Test
    void calculatePrice_WithFreeEligibleBookButIneligibleType_ShouldNotAddToFreeBooks() {
        // Arrange
        List<Book> books = Arrays.asList(newReleaseBook, regularBook);
        List<Book> freeBooks = new ArrayList<>();

        // Act
        BigDecimal totalPrice = pricingService.calculatePrice(books, 10, freeBooks);

        // Assert
        // Expected: 39.99 (new release) + 0 (regular is free) = 39.99
        assertEquals(new BigDecimal("39.99").setScale(2, RoundingMode.HALF_UP), totalPrice);
        assertEquals(1, freeBooks.size());
        assertEquals(regularBook, freeBooks.get(0));
    }
}

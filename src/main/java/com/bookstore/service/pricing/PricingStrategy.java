package com.bookstore.service.pricing;

import com.bookstore.model.Book;
import com.bookstore.model.BookType;

import java.math.BigDecimal;

/**
 * Strategy interface for calculating book prices
 */
public interface PricingStrategy {
    /**
     * Calculate the price of a book
     *
     * @param book     The book to calculate the price for
     * @param isBundle Whether the book is part of a bundle of 3 or more books
     * @return The calculated price
     */
    BigDecimal calculatePrice(Book book, boolean isBundle);

    /**
     * Get the book type this strategy applies to
     *
     * @return The book type
     */
    BookType getType();
}

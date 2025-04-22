package com.bookstore.service.pricing;

import org.springframework.stereotype.Component;

import com.bookstore.model.Book;
import com.bookstore.model.BookType;

import java.math.BigDecimal;

/**
 * Pricing strategy for new release books
 * Price is always 100% of the base price
 */
@Component
public class NewReleasePricingStrategy implements PricingStrategy {
    @Override
    public BigDecimal calculatePrice(Book book, boolean isBundle) {
        // New releases are always full price
        return book.getBasePrice();
    }

    @Override
    public BookType getType() {
        return BookType.NEW_RELEASE;
    }
}

package com.bookstore.service.pricing;

import org.springframework.stereotype.Component;

import com.bookstore.model.Book;
import com.bookstore.model.BookType;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Pricing strategy for regular books
 * Price is 100% of the base price, but can be deducted by 10% if bought in a bundle of 3 books or more
 */
@Component
public class RegularPricingStrategy implements PricingStrategy {
    private static final BigDecimal BUNDLE_DISCOUNT_FACTOR = new BigDecimal("0.9");

    @Override
    public BigDecimal calculatePrice(Book book, boolean isBundle) {
        BigDecimal price = book.getBasePrice();

        // Apply 10% discount for bundles of 3 or more
        if (isBundle) {
            price = price.multiply(BUNDLE_DISCOUNT_FACTOR);
        }

        // Round to 2 decimal places
        return price.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BookType getType() {
        return BookType.REGULAR;
    }
}

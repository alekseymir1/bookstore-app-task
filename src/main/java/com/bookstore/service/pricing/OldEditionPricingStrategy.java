package com.bookstore.service.pricing;

import org.springframework.stereotype.Component;

import com.bookstore.model.Book;
import com.bookstore.model.BookType;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Pricing strategy for old edition books
 * Price is discounted by 20%, can be additionally deducted by 5% if bought in a bundle of 3 books or more
 */
@Component
public class OldEditionPricingStrategy implements PricingStrategy {
    private static final BigDecimal DISCOUNT_FACTOR = new BigDecimal("0.8");
    private static final BigDecimal BUNDLE_DISCOUNT_FACTOR = new BigDecimal("0.95");

    @Override
    public BigDecimal calculatePrice(Book book, boolean isBundle) {
        // Apply 20% discount always
        BigDecimal price = book.getBasePrice().multiply(DISCOUNT_FACTOR);

        // Apply additional 5% discount for bundles of 3 or more
        if (isBundle) {
            price = price.multiply(BUNDLE_DISCOUNT_FACTOR);
        }

        // Round to 2 decimal places
        return price.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BookType getType() {
        return BookType.OLD_EDITION;
    }
}

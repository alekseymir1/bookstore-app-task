package com.bookstore.service.pricing;

import org.springframework.stereotype.Service;

import com.bookstore.model.Book;
import com.bookstore.service.LoyaltyService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import lombok.RequiredArgsConstructor;

/**
 * Service for calculating book prices
 */
@Service
@RequiredArgsConstructor
public class PricingService {

    private final PricingStrategyFactory pricingStrategyFactory;

    private final LoyaltyService loyaltyService;

    /**
     * Calculate the total price for a list of books, applying loyalty points if applicable
     *
     * @param books         The books to calculate the price for
     * @param loyaltyPoints The customer's loyalty points
     * @param freeBooksOut  List to store the titles of free books
     * @return The total price
     */
    public BigDecimal calculatePrice(List<Book> books, int loyaltyPoints, List<Book> freeBooksOut) {
        AtomicReference<BigDecimal> total = new AtomicReference<>(BigDecimal.ZERO);

        // First, identify all free books
        books.stream()
                .sorted((a, b) -> (loyaltyService.isEligibleForFreeRedemption(a) ? 1 : 0) - (loyaltyService.isEligibleForFreeRedemption(b) ? 1 : 0))
                .sorted(Comparator.comparing(Book::getBasePrice))
                .forEach(book -> {
                    if (isFreeBook(loyaltyPoints, freeBooksOut, book)) {
                        // Add the book to the free books list
                        freeBooksOut.add(book);
                    } else {
                        boolean bundle = freeBooksOut.isEmpty() ? books.size() >= 3 : books.size() >= 4;
                        total.set(total.get().add(calculateBookPrice(book, bundle)));
                    }
                });

        return total.get().setScale(2, RoundingMode.HALF_UP);
    }

    private boolean isFreeBook(int loyaltyPoints, List<Book> freeBooksOut, Book book) {
        return freeBooksOut.isEmpty() && loyaltyService.hasEnoughPointsForFreeBook(loyaltyPoints) && loyaltyService.isEligibleForFreeRedemption(book);
    }

    /**
     * Calculate the price of a single book
     *
     * @param book   The book to calculate the price for
     * @param bundle Whether the book is part of a bundle of 3 or more
     * @return The calculated price
     */
    public BigDecimal calculateBookPrice(Book book, boolean bundle) {
        // Use the strategy pattern to calculate the price based on book type
        return pricingStrategyFactory.getStrategy(book.getType())
                .calculatePrice(book, bundle);
    }
}

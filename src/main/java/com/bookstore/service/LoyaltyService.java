package com.bookstore.service;

import org.springframework.stereotype.Service;

import com.bookstore.model.Book;
import com.bookstore.model.BookType;

/**
 * Service for managing customer loyalty points and free book redemption
 */
@Service
public class LoyaltyService {

    /**
     * Check if a book is eligible for free redemption with loyalty points
     *
     * @param book The book to check
     * @return True if the book is eligible, false otherwise
     */
    public boolean isEligibleForFreeRedemption(Book book) {
        // Only regular and old edition books can be free with loyalty points
        return book.getType() != BookType.NEW_RELEASE;
    }

    /**
     * Check if a customer has enough loyalty points for a free book
     *
     * @param loyalityPoints The loyalty points to check
     * @return True if there are enough points, false otherwise
     */
    public boolean hasEnoughPointsForFreeBook(int loyalityPoints) {
        return loyalityPoints >= 10;
    }
}

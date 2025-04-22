package com.bookstore.service.pricing;

import org.springframework.stereotype.Component;

import com.bookstore.model.BookType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Factory for creating pricing strategies based on book type
 */
@Component
public class PricingStrategyFactory {
    private final Map<BookType, PricingStrategy> pricingStrategiesByType;

    public PricingStrategyFactory(List<PricingStrategy> strategies) {
        this.pricingStrategiesByType = strategies.stream()
                .collect(Collectors.toMap(
                        PricingStrategy::getType,
                        strategy -> strategy
                ));
    }

    /**
     * Get the appropriate pricing strategy for a book type
     *
     * @param bookType The type of book
     * @return The pricing strategy for the book type
     */
    public PricingStrategy getStrategy(BookType bookType) {
        PricingStrategy strategy = pricingStrategiesByType.get(bookType);
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown book type: " + bookType);
        }
        return strategy;
    }
}

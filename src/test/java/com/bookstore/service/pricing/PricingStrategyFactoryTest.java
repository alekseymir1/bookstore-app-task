package com.bookstore.service.pricing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bookstore.model.BookType;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PricingStrategyFactoryTest {

    private PricingStrategy newReleasePricingStrategy;
    private PricingStrategy regularPricingStrategy;
    private PricingStrategy oldEditionPricingStrategy;
    private PricingStrategyFactory factory;

    @BeforeEach
    void setUp() {
        // Create mock strategies
        newReleasePricingStrategy = mock(PricingStrategy.class);
        when(newReleasePricingStrategy.getType()).thenReturn(BookType.NEW_RELEASE);

        regularPricingStrategy = mock(PricingStrategy.class);
        when(regularPricingStrategy.getType()).thenReturn(BookType.REGULAR);

        oldEditionPricingStrategy = mock(PricingStrategy.class);
        when(oldEditionPricingStrategy.getType()).thenReturn(BookType.OLD_EDITION);

        // Create factory with mock strategies
        List<PricingStrategy> strategies = Arrays.asList(
                newReleasePricingStrategy,
                regularPricingStrategy,
                oldEditionPricingStrategy
        );
        factory = new PricingStrategyFactory(strategies);
    }

    @Test
    void getStrategy_WithNewRelease_ShouldReturnNewReleaseStrategy() {
        // Act
        PricingStrategy strategy = factory.getStrategy(BookType.NEW_RELEASE);

        // Assert
        assertSame(newReleasePricingStrategy, strategy);
    }

    @Test
    void getStrategy_WithRegular_ShouldReturnRegularStrategy() {
        // Act
        PricingStrategy strategy = factory.getStrategy(BookType.REGULAR);

        // Assert
        assertSame(regularPricingStrategy, strategy);
    }

    @Test
    void getStrategy_WithOldEdition_ShouldReturnOldEditionStrategy() {
        // Act
        PricingStrategy strategy = factory.getStrategy(BookType.OLD_EDITION);

        // Assert
        assertSame(oldEditionPricingStrategy, strategy);
    }

    @Test
    void getStrategy_WithUnknownType_ShouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> factory.getStrategy(null));
    }
}
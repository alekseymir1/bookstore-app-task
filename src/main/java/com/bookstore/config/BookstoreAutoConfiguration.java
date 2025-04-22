package com.bookstore.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.bookstore.controller.BookController;
import com.bookstore.controller.CustomerController;
import com.bookstore.controller.PurchaseController;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.CustomerRepository;
import com.bookstore.service.BookService;
import com.bookstore.service.CustomerService;
import com.bookstore.service.LoyaltyService;
import com.bookstore.service.PurchaseService;
import com.bookstore.service.pricing.NewReleasePricingStrategy;
import com.bookstore.service.pricing.OldEditionPricingStrategy;
import com.bookstore.service.pricing.PricingService;
import com.bookstore.service.pricing.PricingStrategy;
import com.bookstore.service.pricing.PricingStrategyFactory;
import com.bookstore.service.pricing.RegularPricingStrategy;

import java.util.List;

/**
 * Auto-configuration for Bookstore application
 * This class provides beans for all services and controllers if they are not already defined
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.bookstore.repository")
@EntityScan(basePackages = "com.bookstore.model")
@Import({DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class BookstoreAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public BookService bookService(BookRepository bookRepository) {
        return new BookService(bookRepository);
    }

    @Bean
    @ConditionalOnMissingBean
    public CustomerService customerService(CustomerRepository customerRepository) {
        return new CustomerService(customerRepository);
    }

    @Bean
    @ConditionalOnMissingBean
    public LoyaltyService loyaltyService() {
        return new LoyaltyService();
    }

    @Bean
    @ConditionalOnMissingBean
    public NewReleasePricingStrategy newReleasePricingStrategy() {
        return new NewReleasePricingStrategy();
    }

    @Bean
    @ConditionalOnMissingBean
    public OldEditionPricingStrategy oldEditionPricingStrategy() {
        return new OldEditionPricingStrategy();
    }

    @Bean
    @ConditionalOnMissingBean
    public RegularPricingStrategy regularPricingStrategy() {
        return new RegularPricingStrategy();
    }

    @Bean
    @ConditionalOnMissingBean
    public PricingStrategyFactory pricingStrategyFactory(List<PricingStrategy> strategies) {
        return new PricingStrategyFactory(strategies);
    }

    @Bean
    @ConditionalOnMissingBean
    public PricingService pricingService(PricingStrategyFactory pricingStrategyFactory, LoyaltyService loyaltyService) {
        return new PricingService(pricingStrategyFactory, loyaltyService);
    }

    @Bean
    @ConditionalOnMissingBean
    public PurchaseService purchaseService(PricingService pricingService, CustomerService customerService) {
        return new PurchaseService(pricingService, customerService);
    }

    @Bean
    @ConditionalOnMissingBean
    public BookController bookController(BookService bookService) {
        return new BookController(bookService);
    }

    @Bean
    @ConditionalOnMissingBean
    public CustomerController customerController(CustomerService customerService) {
        return new CustomerController(customerService);
    }

    @Bean
    @ConditionalOnMissingBean
    public PurchaseController purchaseController(BookService bookService, CustomerService customerService, PurchaseService purchaseService) {
        return new PurchaseController(bookService, customerService, purchaseService);
    }
}

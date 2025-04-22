package com.bookstore.service;

import org.springframework.stereotype.Service;

import com.bookstore.dto.PurchaseResponse;
import com.bookstore.model.Book;
import com.bookstore.model.Customer;
import com.bookstore.service.pricing.PricingService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PricingService pricingService;
    private final CustomerService customerService;

    public PurchaseResponse purchase(Customer customer, List<Book> books) {
        // Calculate price and apply loyalty points
        List<Book> freeBooks = new ArrayList<>();
        BigDecimal totalPrice = pricingService.calculatePrice(books, customer.getLoyaltyPoints(), freeBooks);
        int customerPoints = customer.getLoyaltyPoints();
        // Update customer loyalty points
        if (!freeBooks.isEmpty()) {
            customerPoints = 0;
        }
        customerPoints = customerPoints + books.size() - freeBooks.size();
        customer.setLoyaltyPoints(customerPoints);
        customerService.updateCustomer(customer.getId(), customer);
        // Create response
        PurchaseResponse response = new PurchaseResponse();
        response.setTotalPrice(totalPrice);
        response.setLoyaltyPointsEarned(customerPoints);
        response.setBooks(books);
        response.setFreeBooks(freeBooks);
        return response;
    }
}


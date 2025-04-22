package com.bookstore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.dto.PricingRequest;
import com.bookstore.dto.PricingResponse;
import com.bookstore.model.Book;
import com.bookstore.model.Customer;
import com.bookstore.service.BookService;
import com.bookstore.service.CustomerService;
import com.bookstore.service.pricing.PricingService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

/**
 * Controller for calculating book prices
 */
@RestController
@RequestMapping("/api/pricing")
@RequiredArgsConstructor
public class PricingController {

    private final BookService bookService;
    private final CustomerService customerService;
    private final PricingService pricingService;

    /**
     * Calculate price for a list of books for a customer
     * 
     * @param request The pricing request containing customer ID and book IDs
     * @return The calculated price and any free books
     */
    @PostMapping("/calculate")
    public ResponseEntity<PricingResponse> calculatePrice(@RequestBody PricingRequest request) {
        // Validate customer exists
        Optional<Customer> customerOpt = customerService.getCustomerById(request.getCustomerId());
        if (customerOpt.isEmpty()) {
            throw new IllegalArgumentException("Customer not found");
        }
        Customer customer = customerOpt.get();

        // Get books from request
        List<Book> books = new ArrayList<>();
        for (Long bookId : request.getBookIds()) {
            Optional<Book> bookOpt = bookService.getBookById(bookId);
            if (bookOpt.isEmpty()) {
                throw new IllegalArgumentException("Book not found");
            }
            books.add(bookOpt.get());
        }

        // Calculate price
        List<Book> freeBooks = new ArrayList<>();
        BigDecimal totalPrice = pricingService.calculatePrice(books, customer.getLoyaltyPoints(), freeBooks);

        // Create response
        PricingResponse response = new PricingResponse();
        response.setTotalPrice(totalPrice);
        response.setFreeBooks(freeBooks);
        response.setBooks(books);

        return ResponseEntity.ok(response);
    }
}
package com.bookstore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.dto.PurchaseRequest;
import com.bookstore.dto.PurchaseResponse;
import com.bookstore.model.Book;
import com.bookstore.model.Customer;
import com.bookstore.service.BookService;
import com.bookstore.service.CustomerService;
import com.bookstore.service.PurchaseService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

/**
 * Controller for managing book purchases
 */
@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final BookService bookService;

    private final CustomerService customerService;

    private final PurchaseService purchaseService;

    /**
     * Purchase Endpoint
     */
    @PostMapping
    public ResponseEntity<PurchaseResponse> purchaseBooks(@RequestBody PurchaseRequest request) {
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

        PurchaseResponse response = purchaseService.purchase(customer, books);
        return ResponseEntity.ok(response);
    }

}
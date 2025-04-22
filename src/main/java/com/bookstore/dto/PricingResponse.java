package com.bookstore.dto;

import com.bookstore.model.Book;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

/**
 * Response DTO for pricing calculation
 */
@Data
public class PricingResponse {
    private BigDecimal totalPrice;
    private List<Book> freeBooks;
    private List<Book> books;
}
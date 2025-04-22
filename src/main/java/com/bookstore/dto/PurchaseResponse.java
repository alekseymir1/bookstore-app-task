package com.bookstore.dto;

import com.bookstore.model.Book;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class PurchaseResponse {
    private BigDecimal totalPrice;
    private int loyaltyPointsEarned;
    private List<Book> freeBooks;
    private List<Book> books;
}

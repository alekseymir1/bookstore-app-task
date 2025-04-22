package com.bookstore.dto;

import java.util.List;

import lombok.Data;

@Data
public class PurchaseRequest {
    private Long customerId;
    private List<Long> bookIds;
}

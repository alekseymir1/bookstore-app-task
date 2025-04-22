package com.bookstore.dto;

import java.util.List;

import lombok.Data;

/**
 * Request DTO for pricing calculation
 */
@Data
public class PricingRequest {
    private Long customerId;
    private List<Long> bookIds;
}
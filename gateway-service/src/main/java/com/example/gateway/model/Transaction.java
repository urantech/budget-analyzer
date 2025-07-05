package com.example.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a bank transaction.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private Long id;
    private Long userId;
    private String description;
    private BigDecimal amount;
    private String category;
    private LocalDateTime timestamp;

    public static Transaction createMockTransaction(Long userId) {
        return Transaction.builder()
                .id(1L)
                .userId(userId)
                .description("Mock Transaction")
                .amount(new BigDecimal("100.00"))
                .category("Other")
                .timestamp(LocalDateTime.now())
                .build();
    }
}

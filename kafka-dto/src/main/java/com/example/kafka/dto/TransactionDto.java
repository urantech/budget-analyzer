package com.example.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for a bank transaction.
 * Used for Kafka communication between services.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private Long id;
    private Long userId;
    private String description;
    private BigDecimal amount;
    private String category;
    private LocalDateTime timestamp;
}

package com.example.transaction.service;

import com.example.kafka.dto.TransactionDto;
import com.example.transaction.model.Transaction;
import com.example.transaction.model.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    
    private final TransactionRepository transactionRepository;
    
    public void save(TransactionDto transactionDto) {
        Transaction transaction = Transaction.builder()
                .userId(transactionDto.getUserId())
                .description(transactionDto.getDescription())
                .amount(transactionDto.getAmount())
                .category(transactionDto.getCategory())
                .timestamp(transactionDto.getTimestamp())
                .build();
        transactionRepository.save(transaction);
    }

    public List<TransactionDto> getUserTransactions(Long userId, LocalDate fromDate, LocalDate toDate) {
        if (userId == null || fromDate == null || toDate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "All parameters must be non-null");
        }
        if (fromDate.isAfter(toDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fromDate must be before or equal to toDate");
        }

        List<Transaction> transactions = transactionRepository.findByUserIdAndTimestampBetween(
                userId,
                LocalDateTime.of(fromDate, LocalTime.MIN),
                LocalDateTime.of(toDate, LocalTime.MAX)
        );

        return transactions.stream()
                .map(trans -> TransactionDto.builder()
                        .description(trans.getDescription())
                        .amount(trans.getAmount())
                        .category(trans.getCategory())
                        .timestamp(trans.getTimestamp())
                        .build())
                .toList();
    }
}

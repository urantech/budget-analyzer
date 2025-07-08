package com.example.gateway.service;

import com.example.gateway.model.Transaction;
import com.example.gateway.publisher.KafkaMessagePublisher;
import com.example.kafka.dto.TransactionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final KafkaMessagePublisher publisher;

    public void sendTransaction(Transaction transaction) {
        TransactionDto transactionDto = TransactionDto.builder()
                .id(transaction.getId())
                .userId(transaction.getUserId())
                .description(transaction.getDescription())
                .amount(transaction.getAmount())
                .category(transaction.getCategory())
                .timestamp(transaction.getTimestamp())
                .build();

        publisher.publish(transactionDto);
    }
}

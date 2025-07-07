package com.example.gateway.service;

import com.example.gateway.model.Transaction;
import com.example.kafka.dto.TransactionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final KafkaTemplate<String, TransactionDto> kafkaTemplate;

    @Value("${kafka.topics.transactions}")
    private String transactionsTopic;

    /**
     * Sends a transaction to the Kafka topic.
     *
     * @param transaction The transaction to send
     */
    public void sendTransaction(Transaction transaction) {
        log.info("Sending transaction to Kafka: {}", transaction);

        TransactionDto transactionDto = TransactionDto.builder()
                .id(transaction.getId())
                .userId(transaction.getUserId())
                .description(transaction.getDescription())
                .amount(transaction.getAmount())
                .category(transaction.getCategory())
                .timestamp(transaction.getTimestamp())
                .build();

        kafkaTemplate.send(transactionsTopic, String.valueOf(transaction.getUserId()), transactionDto);
        log.info("Transaction sent successfully");
    }
}

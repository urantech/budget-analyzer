package com.example.gateway.service;

import com.example.gateway.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topics.transactions}")
    private String transactionsTopic;

    /**
     * Sends a transaction to the Kafka topic.
     *
     * @param transaction The transaction to send
     */
    public void sendTransaction(Transaction transaction) {
        log.info("Sending transaction to Kafka: {}", transaction);
        kafkaTemplate.send(transactionsTopic, String.valueOf(transaction.getUserId()), transaction);
        log.info("Transaction sent successfully");
    }
}

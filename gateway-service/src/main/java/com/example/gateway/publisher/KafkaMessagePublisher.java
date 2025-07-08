package com.example.gateway.publisher;

import com.example.kafka.dto.TransactionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaMessagePublisher {

    private final KafkaTemplate<String, TransactionDto> kafkaTemplate;

    @Value("${kafka.topics.transactions}")
    private String transactionsTopic;

    public void publish(TransactionDto transactionDto) {
        log.info("Sending transaction to Kafka: {}", transactionDto);
        try {
            CompletableFuture<SendResult<String, TransactionDto>> future = kafkaTemplate
                    .send(transactionsTopic, String.valueOf(transactionDto.getUserId()), transactionDto);
            future.whenComplete((res, ex) -> {
                if (ex == null) {
                    log.info("Transaction sent successfully");
                } else {
                    log.error("Unable to send transaction");
                }
            });
        } catch (Exception e) {
            log.error("Unable to send transaction {}", e.getMessage());
        }
    }
}

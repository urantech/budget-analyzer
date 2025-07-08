package com.example.document.publisher;

import com.example.kafka.dto.TransactionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaMessagePublisher {

    private final KafkaTemplate<String, TransactionDto> kafkaTemplate;

    public void publish(String topic, TransactionDto transactionDto) {
        CompletableFuture<SendResult<String, TransactionDto>> future = kafkaTemplate.send(topic, transactionDto);
        future.whenComplete((res, ex) -> {
            if (ex == null) {
                log.info("Transaction {} sent successfully", transactionDto.getId());
            } else {
                log.error("Unable to send transaction {}", transactionDto.getId());
            }
        });
    }
}

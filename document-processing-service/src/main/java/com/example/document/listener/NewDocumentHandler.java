package com.example.document.listener;

import com.example.kafka.dto.TransactionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NewDocumentHandler {

    @KafkaListener(topics = "new.document")
    public void listen(TransactionDto transactionDto) {
        log.info("Received new document event with transaction ID: {}", transactionDto.getId());
        processTransaction(transactionDto);
    }

    private void processTransaction(TransactionDto transaction) {
        // Add your business logic here
        log.info("Processing transaction: {}", transaction);
    }
}

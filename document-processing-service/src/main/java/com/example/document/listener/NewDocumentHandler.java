package com.example.document.listener;

import com.example.document.service.DocumentProcessor;
import com.example.kafka.dto.TransactionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NewDocumentHandler {

    private final DocumentProcessor processor;

    @KafkaListener(topics = "new.document")
    public void listen(TransactionDto transactionDto) {
        log.info("Received new document event with transaction ID: {}", transactionDto.getId());
        processor.process(transactionDto);
    }
}

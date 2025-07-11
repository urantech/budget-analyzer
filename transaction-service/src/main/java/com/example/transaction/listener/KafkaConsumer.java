package com.example.transaction.listener;

import com.example.kafka.dto.TransactionDto;
import com.example.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final TransactionService transactionService;

    @KafkaListener(topics = "new.transactions")
    public void receive(TransactionDto transactionDto) {
        log.info("Received transaction with id {}", transactionDto.getId());
        transactionService.save(transactionDto);
    }
}

package com.example.document.service;

import com.example.kafka.dto.TransactionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentProcessor {

    private final KafkaTemplate<String, TransactionDto> kafkaTemplate;

    @Value("${kafka.topics.transactions}")
    private String transactionTopic;

    @Value("${kafka.topics.analytics}")
    private String analyticsTopic;

    public void process(TransactionDto transactionDto) {
        List<TransactionDto> mockTransactions = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TransactionDto mock = TransactionDto.builder()
                    .id(transactionDto.getId() + i)
                    .userId(transactionDto.getUserId())
                    .description("" + i)
                    .amount(transactionDto.getAmount().multiply(BigDecimal.valueOf(i)))
                    .category(transactionDto.getCategory())
                    .timestamp(transactionDto.getTimestamp().plusMinutes(i))
                    .build();
            mockTransactions.add(mock);
        }

        for (var transaction : mockTransactions) {
            kafkaTemplate.send(transactionTopic, transaction);
            kafkaTemplate.send(analyticsTopic, transaction);
        }
        log.info("Send {} transactions to topics: {}, {}", mockTransactions.size(), transactionTopic, analyticsTopic);
    }
}

package com.example.transaction.listener;

import com.example.kafka.dto.TransactionDto;
import com.example.transaction.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private KafkaConsumer kafkaConsumer;

    @Test
    void testReceive_ShouldCallTransactionServiceSave() {
        Long id = 1L;
        Long userId = 123L;
        String description = "Test transaction";
        BigDecimal amount = new BigDecimal("100.50");
        String category = "Food";
        LocalDateTime timestamp = LocalDateTime.now();

        TransactionDto transactionDto = TransactionDto.builder()
                .id(id)
                .userId(userId)
                .description(description)
                .amount(amount)
                .category(category)
                .timestamp(timestamp)
                .build();

        kafkaConsumer.receive(transactionDto);

        verify(transactionService).save(transactionDto);
    }
}

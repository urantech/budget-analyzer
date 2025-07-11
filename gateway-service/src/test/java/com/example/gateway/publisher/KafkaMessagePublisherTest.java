package com.example.gateway.publisher;

import com.example.kafka.dto.TransactionDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KafkaMessagePublisherTest {

    @Mock
    private KafkaTemplate<String, TransactionDto> kafkaTemplate;

    @InjectMocks
    private KafkaMessagePublisher kafkaMessagePublisher;

    @Captor
    private ArgumentCaptor<String> topicCaptor;

    @Captor
    private ArgumentCaptor<TransactionDto> messageCaptor;

    @Test
    void shouldSendMessage() {
        // Set up the topic name using reflection
        String expectedTopic = "test-topic";
        ReflectionTestUtils.setField(kafkaMessagePublisher, "transactionsTopic", expectedTopic);

        // Create a test transaction
        TransactionDto transaction = TransactionDto.builder()
                .id(1L)
                .build();

        // Mock the KafkaTemplate response
        CompletableFuture<SendResult<String, TransactionDto>> future = new CompletableFuture<>();
        when(kafkaTemplate.send(anyString(), any(TransactionDto.class))).thenReturn(future);

        // Call the method under test
        kafkaMessagePublisher.publish(transaction);

        // Verify that the message was sent to the correct topic with the correct payload
        verify(kafkaTemplate).send(topicCaptor.capture(), messageCaptor.capture());

        // Assert that the topic and message are correct
        assertEquals(expectedTopic, topicCaptor.getValue());
        assertEquals(transaction.getId(), messageCaptor.getValue().getId());
    }
}

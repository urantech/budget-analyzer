package com.example.document.listener;

import com.example.document.service.DocumentProcessor;
import com.example.kafka.dto.TransactionDto;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.EmbeddedKafkaZKBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@DirtiesContext
@Import(KafkaConsumerIntegrationTest.TestConfig.class)
@EmbeddedKafka(partitions = 1, brokerProperties = {
        "listeners=PLAINTEXT://localhost:9092", "port=9092"
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KafkaConsumerIntegrationTest {

    public static final String TOPIC = "new.document";

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    private Producer<String, TransactionDto> producer;

    @SpyBean
    private KafkaConsumer consumer;

    @Captor
    private ArgumentCaptor<TransactionDto> transactionCaptor;

    static class TestConfig {
        @Bean
        public EmbeddedKafkaBroker embeddedKafkaBroker() {
            return new EmbeddedKafkaZKBroker(1, true, TOPIC);
        }

        @Bean
        @Primary
        public DocumentProcessor documentProcessor() {
            return mock(DocumentProcessor.class);
        }
    }

    @BeforeEach
    void setUp() {
        Map<String, Object> props = KafkaTestUtils.producerProps(embeddedKafkaBroker);
        DefaultKafkaProducerFactory<String, TransactionDto> producerFactory =
                new DefaultKafkaProducerFactory<>(props, new StringSerializer(), new JsonSerializer<>());
        producer = producerFactory.createProducer();
    }

    @AfterEach
    void tearDown() {
        if (producer != null) {
            producer.close();
        }
    }

    @Test
    void shouldReceiveMessage() {
        TransactionDto transaction = TransactionDto.builder()
                .id(1L)
                .userId(2L)
                .build();

        producer.send(new ProducerRecord<>(TOPIC, transaction));
        producer.flush();

        verify(consumer, timeout(5000).times(1)).receive(transactionCaptor.capture());
        TransactionDto processedTransaction = transactionCaptor.getValue();
        assertEquals(1L, processedTransaction.getId());
    }
}

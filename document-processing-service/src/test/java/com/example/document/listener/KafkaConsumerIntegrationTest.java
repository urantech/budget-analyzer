package com.example.document.listener;

import com.example.document.service.DocumentProcessor;
import com.example.kafka.dto.TransactionDto;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class KafkaConsumerIntegrationTest {

    public static final String TOPIC = "new.document";

    @MockBean
    private DocumentProcessor documentProcessor;

    private Producer<String, TransactionDto> producer;

    @Captor
    private ArgumentCaptor<TransactionDto> transactionCaptor;

    @BeforeEach
    void setUp(@Autowired EmbeddedKafkaBroker embeddedKafkaBroker) {
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
    void shouldReceiveMessage() throws InterruptedException {
        Thread.sleep(2000);
        TransactionDto transaction = TransactionDto.builder()
                .id(1L)
                .userId(2L)
                .build();

        producer.send(new ProducerRecord<>(TOPIC, transaction));
        producer.flush();

        verify(documentProcessor, timeout(5000).times(1)).process(transactionCaptor.capture());
        TransactionDto processedTransaction = transactionCaptor.getValue();
        assertEquals(1L, processedTransaction.getId());
    }
}

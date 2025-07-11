package com.example.gateway.publisher;

import com.example.kafka.dto.TransactionDto;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.EmbeddedKafkaZKBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext
@Import(KafkaMessagePublisherTest.TestConfig.class)
@EmbeddedKafka(partitions = 1, brokerProperties = {
        "listeners=PLAINTEXT://localhost:9092", "port=9092"
})
class KafkaMessagePublisherTest {

    @Autowired
    private KafkaMessagePublisher kafkaMessagePublisher;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    private Consumer<String, TransactionDto> consumer;

    private CountDownLatch latch;

    @Value("${kafka.topics.transactions}")
    private String transactionsTopic;

    static class TestConfig {
        @Bean
        public EmbeddedKafkaBroker embeddedKafkaBroker() {
            return new EmbeddedKafkaZKBroker(1, true, "new.document");
        }
    }

    @BeforeEach
    void setUp() {
        latch = new CountDownLatch(1);

        Map<String, Object> props = KafkaTestUtils.consumerProps("test-group", "true", embeddedKafkaBroker);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        DefaultKafkaConsumerFactory<String, TransactionDto> consumerFactory =
                new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(TransactionDto.class, false));
        consumer = consumerFactory.createConsumer();
        consumer.subscribe(List.of(transactionsTopic));
    }

    @AfterEach
    void tearDown() {
        if (consumer != null) {
            consumer.close();
        }
    }

    @Test
    void shouldSendMessage() throws InterruptedException {
        TransactionDto transaction = TransactionDto.builder()
                .id(1L)
                .build();

        kafkaMessagePublisher.publish(transaction);

        new Thread(() -> {
            ConsumerRecords<String, TransactionDto> records = consumer.poll(Duration.ofSeconds(5));
            for (var consumerRecord : records) {
                TransactionDto receivedTransaction = consumerRecord.value();
                if (receivedTransaction.getId().equals(transaction.getId())) {
                    latch.countDown();
                    break;
                }
            }
        }).start();

        boolean received = latch.await(10, TimeUnit.SECONDS);
        assertTrue(received);
    }
}

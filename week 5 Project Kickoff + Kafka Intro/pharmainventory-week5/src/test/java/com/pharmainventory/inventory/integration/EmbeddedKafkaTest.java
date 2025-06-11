package com.pharmainventory.inventory.integration;

import com.pharmainventory.inventory.event.MedicineCreatedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = { "medicine-created-topic" })
public class EmbeddedKafkaTest {

    @Test
    public void contextLoads() {
        // Sanity check: Spring context and embedded Kafka broker start up correctly.
    }
}

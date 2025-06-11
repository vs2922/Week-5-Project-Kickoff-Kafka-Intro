package com.pharmainventory.inventory.integration;

import com.pharmainventory.inventory.consumer.MedicineConsumer;
import com.pharmainventory.inventory.event.MedicineCreatedEvent;
import com.pharmainventory.inventory.producer.MedicineProducer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = { "medicine-created-topic" })
public class KafkaIntegrationTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @Autowired
    private MedicineProducer producer;

    @Test
    public void testPublishAndConsume() throws Exception {
        BlockingQueue<ConsumerRecord<String, MedicineCreatedEvent>> records = new LinkedBlockingQueue<>();

        Map<String, Object> props = KafkaTestUtils.consumerProps(
            "testGroup", "false", embeddedKafka);
        DefaultKafkaConsumerFactory<String, MedicineCreatedEvent> cf =
            new DefaultKafkaConsumerFactory<>(props);
        ContainerProperties containerProperties = new ContainerProperties("medicine-created-topic");
        KafkaMessageListenerContainer<String, MedicineCreatedEvent> container =
            new KafkaMessageListenerContainer<>(cf, containerProperties);

        container.setupMessageListener((org.springframework.kafka.listener.MessageListener<String, MedicineCreatedEvent>) record -> {
            records.add(record);
        });
        container.start();
        ContainerTestUtils.waitForAssignment(container, embeddedKafka.getPartitionsPerTopic());

        // Publish event
        MedicineCreatedEvent event = new MedicineCreatedEvent(42L, "TestMed", "PharmaCo", LocalDate.now().plusDays(30));
        producer.publish(event);

        // Assert consumed
        ConsumerRecord<String, MedicineCreatedEvent> received = records.take();
        assertThat(received.value().getMedicineId()).isEqualTo(42L);
        assertThat(received.value().getName()).isEqualTo("TestMed");

        container.stop();
    }
}

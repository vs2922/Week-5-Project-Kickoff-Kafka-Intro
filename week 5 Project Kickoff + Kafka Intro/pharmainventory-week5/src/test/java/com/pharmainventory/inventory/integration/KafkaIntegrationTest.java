package com.pharmainventory.inventory.integration;

import com.pharmainventory.inventory.event.MedicineCreatedEvent;
import com.pharmainventory.inventory.producer.MedicineProducer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.test.utils.ContainerTestUtils;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.pharmainventory.inventory.config.KafkaTopics.MEDICINE_CREATED;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = { MEDICINE_CREATED })
public class KafkaIntegrationTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @Autowired
    private MedicineProducer producer;

    @Test
    public void testPublishAndConsume() throws Exception {
        BlockingQueue<ConsumerRecord<String, MedicineCreatedEvent>> records = new LinkedBlockingQueue<>();

        Map<String, Object> props = KafkaTestUtils.consumerProps(
            "testGroup", "false", embeddedKafka
        );
        DefaultKafkaConsumerFactory<String, MedicineCreatedEvent> cf =
            new DefaultKafkaConsumerFactory<>(props);
        ContainerProperties containerProps = new ContainerProperties(MEDICINE_CREATED);
        KafkaMessageListenerContainer<String, MedicineCreatedEvent> container =
            new KafkaMessageListenerContainer<>(cf, containerProps);

        container.setupMessageListener((record) -> records.add(record));
        container.start();
        ContainerTestUtils.waitForAssignment(container, embeddedKafka.getPartitionsPerTopic());

        MedicineCreatedEvent event = new MedicineCreatedEvent(42L, "TestMed", "PharmaCo", LocalDate.now().plusDays(30));
        producer.publish(event);

        ConsumerRecord<String, MedicineCreatedEvent> received = records.take();
        assertThat(received.value().getMedicineId()).isEqualTo(42L);
        assertThat(received.value().getName()).isEqualTo("TestMed");

        container.stop();
    }
}

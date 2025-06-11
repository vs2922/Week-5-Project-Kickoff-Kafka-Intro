package com.pharmainventory.inventory.producer;

import com.pharmainventory.inventory.event.MedicineCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import static com.pharmainventory.inventory.config.KafkaConfig.MEDICINE_TOPIC;

@Service
public class MedicineProducer {

    private final KafkaTemplate<String, MedicineCreatedEvent> kafkaTemplate;

    public MedicineProducer(KafkaTemplate<String, MedicineCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(MedicineCreatedEvent event) {
        kafkaTemplate.send(MEDICINE_TOPIC, event.getMedicineId().toString(), event);
    }
}

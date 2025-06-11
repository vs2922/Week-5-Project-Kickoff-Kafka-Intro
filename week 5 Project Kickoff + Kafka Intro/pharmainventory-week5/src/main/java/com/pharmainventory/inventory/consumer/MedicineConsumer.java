package com.pharmainventory.inventory.consumer;

import com.pharmainventory.inventory.event.MedicineCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import static com.pharmainventory.inventory.config.KafkaConfig.MEDICINE_TOPIC;

@Service
@Slf4j
public class MedicineConsumer {

    @KafkaListener(topics = MEDICINE_TOPIC, groupId = "pharma-inventory-group")
    public void listen(MedicineCreatedEvent event) {
        log.info("Received MedicineCreatedEvent: {}", event);
        // e.g., update cache, trigger further processing...
    }
}

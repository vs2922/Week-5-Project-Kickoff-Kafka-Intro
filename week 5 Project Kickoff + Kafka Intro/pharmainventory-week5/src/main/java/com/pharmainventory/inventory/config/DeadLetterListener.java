package com.pharmainventory.inventory.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service @Slf4j
public class DeadLetterListener {
    @KafkaListener(topics = "${spring.kafka.template.default-topic}.DLT")
    public void listenDLT(String record) {
        log.error("Received dead-letter record: {}", record);
        // Persist or alert...
    }
}

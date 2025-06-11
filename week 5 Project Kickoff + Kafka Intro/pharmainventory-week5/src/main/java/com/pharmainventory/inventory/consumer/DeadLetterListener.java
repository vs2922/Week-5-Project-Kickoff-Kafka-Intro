package com.pharmainventory.inventory.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Listens for any messages that failed processing on the main topic
 * and were forwarded to the Dead-Letter Topic (DLT).
 */
@Service
@Slf4j
public class DeadLetterListener {

    /**
     * Consumes records from the dead-letter topic for medicine-created events.
     * The DLT topic name is automatically derived as "<original-topic>.DLT"
     * when using a DefaultErrorHandler or DeadLetterPublishingRecoverer.
     */
    @KafkaListener(
        topics = "medicine-created-topic.DLT",
        groupId = "pharma-inventory-group-dlt"
    )
    public void handleMedicineDlt(String record) {
        log.error("Dead-letter record for medicine-created-topic: {}", record);
        // TODO: persist to database, send alert, or take corrective action
    }

    /**
     * Consumes records from the dead-letter topic for pallet-processed events.
     */
    @KafkaListener(
        topics = "pallet-processed-topic.DLT",
        groupId = "pharma-inventory-group-dlt"
    )
    public void handlePalletDlt(String record) {
        log.error("Dead-letter record for pallet-processed-topic: {}", record);
        // TODO: handle DLT for pallet-processed events
    }

    /**
     * Consumes records from the dead-letter topic for task-completed events.
     */
    @KafkaListener(
        topics = "task-completed-topic.DLT",
        groupId = "pharma-inventory-group-dlt"
    )
    public void handleTaskDlt(String record) {
        log.error("Dead-letter record for task-completed-topic: {}", record);
        // TODO: handle DLT for task-completed events
    }
}

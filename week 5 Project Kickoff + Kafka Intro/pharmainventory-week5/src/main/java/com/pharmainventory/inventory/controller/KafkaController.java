package com.pharmainventory.inventory.controller;

import com.pharmainventory.inventory.event.MedicineCreatedEvent;
import com.pharmainventory.inventory.producer.MedicineProducer;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/kafka")
public class KafkaController {

    private final MedicineProducer producer;

    public KafkaController(MedicineProducer producer) {
        this.producer = producer;
    }

    @PostMapping("/publish")
    public String publishSampleEvent(@RequestParam String name) {
        MedicineCreatedEvent event = new MedicineCreatedEvent(123L, name, "ACME", LocalDate.now().plusMonths(6));
        producer.publish(event);
        return "Event published: " + event;
    }
}

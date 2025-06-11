package com.pharmainventory.inventory.producer;

import com.pharmainventory.inventory.event.PalletProcessedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import static com.pharmainventory.inventory.config.KafkaTopics.PALLET_PROCESSED;

@Service
public class PalletProducer {
    private final KafkaTemplate<String, PalletProcessedEvent> template;
    public PalletProducer(KafkaTemplate<String, PalletProcessedEvent> t){ this.template = t; }
    public void publish(PalletProcessedEvent event) {
        template.send(PALLET_PROCESSED, event.getPalletId().toString(), event);
    }
}

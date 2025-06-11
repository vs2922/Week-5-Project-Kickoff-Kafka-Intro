package com.pharmainventory.inventory.stream;

import com.pharmainventory.inventory.event.MedicineCreatedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageChannel;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;

@Configuration
@EnableBinding(Source.class)
public class StreamConfig {
    @Bean
    public MessageChannel medicineCreatedChannel(Source source) {
        return source.output();
    }
}

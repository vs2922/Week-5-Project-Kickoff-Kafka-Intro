package com.pharmainventory.inventory.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DeadLetterConfig {
    @Bean
    public Map<String, Object> consumerConfigs() {
        var props = new HashMap<String, Object>();
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put("spring.kafka.consumer.max.poll.interval.ms", 300000);
        // additional props...
        return props;
    }
}

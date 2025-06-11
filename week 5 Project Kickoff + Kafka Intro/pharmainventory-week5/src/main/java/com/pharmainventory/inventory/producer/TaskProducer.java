package com.pharmainventory.inventory.producer;

import com.pharmainventory.inventory.event.TaskCompletedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import static com.pharmainventory.inventory.config.KafkaTopics.TASK_COMPLETED;

@Service
public class TaskProducer {
    private final KafkaTemplate<String, TaskCompletedEvent> template;
    public TaskProducer(KafkaTemplate<String, TaskCompletedEvent> t){ this.template = t; }
    public void publish(TaskCompletedEvent event) {
        template.send(TASK_COMPLETED, event.getTaskId().toString(), event);
    }
}

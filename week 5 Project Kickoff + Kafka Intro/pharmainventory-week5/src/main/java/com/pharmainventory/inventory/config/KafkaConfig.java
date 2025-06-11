package com.pharmainventory.inventory.config;

public class KafkaTopics {
    public static final String MEDICINE_CREATED = "medicine-created-topic";
    public static final String PALLET_PROCESSED  = "pallet-processed-topic";
    public static final String TASK_COMPLETED    = "task-completed-topic";
}


@Bean public NewTopic palletProcessedTopic() {
    return new NewTopic(KafkaTopics.PALLET_PROCESSED, 1, (short)1);
}
@Bean public NewTopic taskCompletedTopic() {
    return new NewTopic(KafkaTopics.TASK_COMPLETED, 1, (short)1);
}

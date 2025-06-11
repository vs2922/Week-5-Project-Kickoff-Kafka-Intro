package com.pharmainventory.inventory.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.Instant;

@Data
@AllArgsConstructor
public class TaskCompletedEvent {
    private Long taskId;
    private String completedBy;
    private Instant completedAt;
}

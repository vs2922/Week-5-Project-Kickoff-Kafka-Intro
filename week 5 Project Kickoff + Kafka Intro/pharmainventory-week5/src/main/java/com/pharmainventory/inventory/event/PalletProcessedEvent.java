package com.pharmainventory.inventory.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.Instant;

@Data
@AllArgsConstructor
public class PalletProcessedEvent {
    private Long palletId;
    private String status;          // e.g. "PROCESSED"
    private Instant processedAt;
}

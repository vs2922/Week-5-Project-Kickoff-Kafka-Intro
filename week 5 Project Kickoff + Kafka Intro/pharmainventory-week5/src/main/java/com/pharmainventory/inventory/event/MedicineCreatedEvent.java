package com.pharmainventory.inventory.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class MedicineCreatedEvent {
    private Long medicineId;
    private String name;
    private String manufacturer;
    private LocalDate expiryDate;
}

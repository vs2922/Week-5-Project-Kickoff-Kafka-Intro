package com.pharmainventory.inventory.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineDTO {
    private Long id;

    @NotBlank(message = "Medicine name must not be blank")
    private String name;

    @NotBlank(message = "Manufacturer must not be blank")
    private String manufacturer;

    @NotNull(message = "Expiry date is required")
    private LocalDate expiryDate;

    @NotBlank(message = "Condition must not be blank")
    private String condition;
}

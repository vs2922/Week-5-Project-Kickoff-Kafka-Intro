package com.pharmainventory.inventory.service;

import com.pharmainventory.inventory.dto.MedicineDTO;
import com.github.fge.jsonpatch.JsonPatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InventoryService {
    List<MedicineDTO> listAllMedicines();
    MedicineDTO getMedicine(Long id);
    MedicineDTO createMedicine(MedicineDTO dto);
    MedicineDTO updateMedicine(Long id, MedicineDTO dto);
    void deleteMedicine(Long id);
    List<MedicineDTO> listExpiredMedicines();
    Page<MedicineDTO> listMedicines(Pageable pageable);
    MedicineDTO patchMedicine(Long id, JsonPatch patch);
}

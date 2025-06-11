package com.pharmainventory.inventory.service;

import com.pharmainventory.inventory.dto.MedicineDTO;
import com.pharmainventory.inventory.exception.ResourceNotFoundException;
import com.pharmainventory.inventory.mapper.MedicineMapper;
import com.pharmainventory.inventory.model.Medicine;
import com.pharmainventory.inventory.repository.MedicineRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {
    private final MedicineRepository medicineRepo;
    private final ObjectMapper objectMapper;

    public InventoryServiceImpl(MedicineRepository medicineRepo, ObjectMapper objectMapper) {
        this.medicineRepo = medicineRepo;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineDTO> listAllMedicines() {
        return medicineRepo.findAll().stream()
            .map(MedicineMapper.INSTANCE::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MedicineDTO getMedicine(Long id) {
        Medicine entity = medicineRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Medicine", id));
        return MedicineMapper.INSTANCE.toDto(entity);
    }

    @Override
    public MedicineDTO createMedicine(MedicineDTO dto) {
        Medicine entity = MedicineMapper.INSTANCE.toEntity(dto);
        Medicine saved = medicineRepo.save(entity);
        return MedicineMapper.INSTANCE.toDto(saved);
    }

    @Override
    public MedicineDTO updateMedicine(Long id, MedicineDTO dto) {
        Medicine existing = medicineRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Medicine", id));
        existing.setName(dto.getName());
        existing.setManufacturer(dto.getManufacturer());
        existing.setExpiryDate(dto.getExpiryDate());
        existing.setCondition(dto.getCondition());
        Medicine updated = medicineRepo.save(existing);
        return MedicineMapper.INSTANCE.toDto(updated);
    }

    @Override
    public void deleteMedicine(Long id) {
        Medicine toDelete = medicineRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Medicine", id));
        medicineRepo.delete(toDelete);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineDTO> listExpiredMedicines() {
        return medicineRepo.findByExpiryDateBefore(LocalDate.now()).stream()
            .map(MedicineMapper.INSTANCE::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MedicineDTO> listMedicines(Pageable pageable) {
        return medicineRepo.findAll(pageable)
            .map(MedicineMapper.INSTANCE::toDto);
    }

    @Override
    public MedicineDTO patchMedicine(Long id, JsonPatch patch) {
        Medicine existing = medicineRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Medicine", id));

        // Apply JSON Patch
        JsonNode patchedNode = patch.apply(objectMapper.convertValue(existing, JsonNode.class));
        Medicine patched = objectMapper.treeToValue(patchedNode, Medicine.class);

        // Preserve ID
        patched.setId(id);
        Medicine saved = medicineRepo.save(patched);
        return MedicineMapper.INSTANCE.toDto(saved);
    }
}

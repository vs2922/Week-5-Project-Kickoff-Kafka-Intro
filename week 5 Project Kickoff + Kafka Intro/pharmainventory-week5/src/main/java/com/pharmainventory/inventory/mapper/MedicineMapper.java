package com.pharmainventory.inventory.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.pharmainventory.inventory.model.Medicine;
import com.pharmainventory.inventory.dto.MedicineDTO;

@Mapper
public interface MedicineMapper {
    MedicineMapper INSTANCE = Mappers.getMapper(MedicineMapper.class);

    MedicineDTO toDto(Medicine entity);
    Medicine toEntity(MedicineDTO dto);
}

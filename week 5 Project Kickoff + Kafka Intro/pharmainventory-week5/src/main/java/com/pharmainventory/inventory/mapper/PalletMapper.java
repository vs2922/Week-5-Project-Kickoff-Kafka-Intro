package com.pharmainventory.inventory.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.pharmainventory.inventory.model.Pallet;
import com.pharmainventory.inventory.dto.PalletDTO;

@Mapper
public interface PalletMapper {
    PalletMapper INSTANCE = Mappers.getMapper(PalletMapper.class);

    PalletDTO toDto(Pallet entity);
    Pallet toEntity(PalletDTO dto);
}

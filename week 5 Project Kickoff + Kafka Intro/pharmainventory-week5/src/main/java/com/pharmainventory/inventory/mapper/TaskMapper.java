package com.pharmainventory.inventory.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.pharmainventory.inventory.model.Task;
import com.pharmainventory.inventory.dto.TaskDTO;

@Mapper
public interface TaskMapper {
    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    TaskDTO toDto(Task entity);
    Task toEntity(TaskDTO dto);
}

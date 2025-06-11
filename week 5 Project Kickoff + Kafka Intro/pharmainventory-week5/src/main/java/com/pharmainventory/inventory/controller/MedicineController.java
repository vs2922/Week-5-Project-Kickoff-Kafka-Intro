package com.pharmainventory.inventory.controller;

import com.pharmainventory.inventory.dto.MedicineDTO;
import com.pharmainventory.inventory.event.MedicineCreatedEvent;
import com.pharmainventory.inventory.producer.MedicineProducer;
import com.pharmainventory.inventory.service.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.PagedResourcesAssembler;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {
    private final InventoryService svc;
    private final MedicineProducer producer;
    private final PagedResourcesAssembler<MedicineDTO> assembler;
    private final ObjectMapper objectMapper;

    public MedicineController(
        InventoryService svc,
        MedicineProducer producer,
        PagedResourcesAssembler<MedicineDTO> assembler,
        ObjectMapper objectMapper
    ) {
        this.svc = svc;
        this.producer = producer;
        this.assembler = assembler;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public PagedModel<EntityModel<MedicineDTO>> list(Pageable pageable) {
        Page<MedicineDTO> page = svc.listMedicines(pageable);
        return assembler.toModel(page, dto ->
            EntityModel.of(dto,
                linkTo(methodOn(MedicineController.class).get(dto.getId())).withSelfRel()
            )
        );
    }

    @GetMapping("/{id}")
    public EntityModel<MedicineDTO> get(@PathVariable Long id) {
        MedicineDTO dto = svc.getMedicine(id);
        return EntityModel.of(dto,
            linkTo(methodOn(MedicineController.class).list(Pageable.unpaged())).withRel("medicines"),
            linkTo(methodOn(MedicineController.class).get(id)).withSelfRel()
        );
    }

    @PostMapping
    public MedicineDTO create(@Valid @RequestBody MedicineDTO dto) {
        MedicineDTO saved = svc.createMedicine(dto);
        // publish event
        producer.publish(new MedicineCreatedEvent(
            saved.getId(), saved.getName(), saved.getManufacturer(), saved.getExpiryDate()
        ));
        return saved;
    }

    @PutMapping("/{id}")
    public MedicineDTO update(@PathVariable Long id, @Valid @RequestBody MedicineDTO dto) {
        return svc.updateMedicine(id, dto);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    public MedicineDTO patch(@PathVariable Long id, @RequestBody JsonPatch patch) {
        return svc.patchMedicine(id, patch);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        svc.deleteMedicine(id);
    }
}

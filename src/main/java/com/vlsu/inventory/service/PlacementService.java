package com.vlsu.inventory.service;

import com.vlsu.inventory.dto.model.PlacementDto;
import com.vlsu.inventory.model.Placement;
import com.vlsu.inventory.repository.PlacementRepository;
import com.vlsu.inventory.util.exception.ResourceHasDependenciesException;
import com.vlsu.inventory.util.exception.ResourceNotFoundException;
import com.vlsu.inventory.util.mapping.PlacementMappingUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class PlacementService {

    PlacementRepository placementRepository;

    public List<PlacementDto.Response.Default> getAll() {
        List<Placement> placements = placementRepository.findAll();
        return placements.stream().map(PlacementMappingUtils::toDto).collect(Collectors.toList());
    }

    public PlacementDto.Response.Default getById(Long id) throws ResourceNotFoundException {
        Placement placement = placementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Placement with id: " + id + " not found"));
        return PlacementMappingUtils.toDto(placement);
    }

    public Placement create(PlacementDto.Request.Create request) {
        Placement placement = PlacementMappingUtils.fromDto(request);
        return placementRepository.save(placement);
    }

    public Placement update(PlacementDto.Request.Update request) throws ResourceNotFoundException {
        Placement placement = placementRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Placement with id: " + request.getId() + " not found"));

        placement.setName(request.getName());
        return placementRepository.save(placement);
    }

    public void delete(Long id) throws ResourceNotFoundException, ResourceHasDependenciesException {
        Placement placement = placementRepository.findWithEquipmentById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Placement with id: " + id + " not found"));
        if (!placement.getEquipment().isEmpty()) {
            throw new ResourceHasDependenciesException("Placement with id: " + id + " has relations with equipment");
        }
        placementRepository.deleteById(id);
    }
}

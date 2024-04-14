package com.vlsu.inventory.service;

import com.vlsu.inventory.model.Placement;
import com.vlsu.inventory.repository.PlacementRepository;
import com.vlsu.inventory.util.exception.ResourceHasDependenciesException;
import com.vlsu.inventory.util.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlacementService {
    private final PlacementRepository placementRepository;

    public PlacementService(PlacementRepository placementRepository) {
        this.placementRepository = placementRepository;
    }

    public List<Placement> getAllPlacements() {
        return placementRepository.findAll();
    }
    public Placement getPlacementById(Long id) throws ResourceNotFoundException {
        return placementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Placement with id: " + id + " not found"));
    }

    public List<Placement> getPlacementByName(String name)  {
        return placementRepository.findByNameStartingWith(name);
    }

    public void createPlacement(Placement placement) {
        placementRepository.save(placement);
    }

    public void updatePlacementById(Long id, Placement placement) throws ResourceNotFoundException {
        Placement placementToUpdate = placementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Placement with id: " + id + " not found"));

        placementToUpdate.setName(placement.getName());
        placementRepository.save(placementToUpdate);
    }

    public void deletePlacementById(Long id) throws ResourceNotFoundException, ResourceHasDependenciesException {
        Placement placementToDelete = placementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Placement with id: " + id + " not found"));
        if (!placementToDelete.getEquipment().isEmpty()) {
            throw new ResourceHasDependenciesException("Placement with id: " + id + " has relations with equipment");
        }
        else if (!placementToDelete.getRents().isEmpty()) {
            throw new ResourceHasDependenciesException("Placement with id: " + id + " has relations with rents");
        }
        placementRepository.deleteById(id);
    }
}

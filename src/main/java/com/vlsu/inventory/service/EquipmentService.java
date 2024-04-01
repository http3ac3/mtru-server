package com.vlsu.inventory.service;

import com.vlsu.inventory.model.Equipment;
import com.vlsu.inventory.repository.EquipmentRepository;
import com.vlsu.inventory.util.PaginationMap;
import com.vlsu.inventory.util.exception.ResourceHasDependenciesException;
import com.vlsu.inventory.util.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;

    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    public Map<String, Object> getAllEquipmentPageable(int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Equipment> pageEquipment = equipmentRepository.findAll(paging);
        List<Equipment> equipment = pageEquipment.getContent();
        PaginationMap<Equipment> paginationMap = new PaginationMap<>(pageEquipment, equipment);
        return paginationMap.getPaginatedMap();
    }

    public Equipment getEquipmentById(Long id) throws ResourceNotFoundException {
        return equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Equipment with id: " + id + " not found"));
    }

    public Equipment getEquipmentByInventoryNumber(String inventoryNumber)
            throws ResourceNotFoundException {
        return equipmentRepository.findByInventoryNumber(inventoryNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Equipment with inventory number: " + inventoryNumber + " not found"));
    }

    public Map<String, Object> getEquipmentByInventoryNumberContainingPageable(
            String inventoryNumberPart, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Equipment> pageEquipment = equipmentRepository
                .findByInventoryNumberContaining(inventoryNumberPart, paging);
        List<Equipment> equipment = pageEquipment.getContent();
        PaginationMap<Equipment> paginationMap = new PaginationMap<>(pageEquipment, equipment);
        return paginationMap.getPaginatedMap();
    }


    public Map<String, Object> getEquipmentByNamePageable(String name, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Equipment> pageEquipment = equipmentRepository.findByName(name, paging);
        List<Equipment> equipment = pageEquipment.getContent();
        PaginationMap<Equipment> paginationMap
                = new PaginationMap<>(pageEquipment, equipment);
        return paginationMap.getPaginatedMap();
    }

    public Map<String, Object> getEquipmentByNameContainingPageable(String name, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Equipment> pageEquipment = equipmentRepository.findByNameContaining(name, paging);
        List<Equipment> equipment = pageEquipment.getContent();
        PaginationMap<Equipment> paginationMap = new PaginationMap<>(pageEquipment, equipment);
        return paginationMap.getPaginatedMap();
    }

    public Map<String, Object> getEquipmentByCommissioningDatePageable(LocalDate date, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Equipment> pageEquipment = equipmentRepository.findByCommissioningDate(date, paging);
        List<Equipment> equipment = pageEquipment.getContent();
        PaginationMap<Equipment> paginationMap = new PaginationMap<>(pageEquipment, equipment);
        return paginationMap.getPaginatedMap();
    }

    public Map<String, Object> getEquipmentByCommissioningActNumberPageable(String actNumber, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Equipment> pageEquipment = equipmentRepository.findByCommissioningActNumber(actNumber, paging);
        List<Equipment> equipment = pageEquipment.getContent();
        PaginationMap<Equipment> paginationMap = new PaginationMap<>(pageEquipment, equipment);
        return paginationMap.getPaginatedMap();
    }

    public Map<String, Object> getEquipmentByDecommissioningDatePageable(LocalDate date, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Equipment> pageEquipment = equipmentRepository.findByDecommissioningDate(date, paging);
        List<Equipment> equipment = pageEquipment.getContent();
        PaginationMap<Equipment> paginationMap = new PaginationMap<>(pageEquipment, equipment);
        return paginationMap.getPaginatedMap();
    }

    public Map<String, Object> getEquipmentByDecommissioningActNumberPageable(String actNumber, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Equipment> pageEquipment = equipmentRepository.findByDecommissioningActNumber(actNumber, paging);
        List<Equipment> equipment = pageEquipment.getContent();
        PaginationMap<Equipment> paginationMap = new PaginationMap<>(pageEquipment, equipment);
        return paginationMap.getPaginatedMap();
    }

    public Map<String, Object> getEquipmentByResponsibleIdPageable(Long responsibleId, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Equipment> pageEquipment = equipmentRepository.findByResponsibleId(responsibleId, paging);
        List<Equipment> equipment = pageEquipment.getContent();
        PaginationMap<Equipment> paginationMap = new PaginationMap<>(pageEquipment, equipment);
        return paginationMap.getPaginatedMap();
    }

    public Map<String, Object> getEquipmentByPlacementIdPageable(Long placementId, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Equipment> pageEquipment = equipmentRepository.findByPlacementId(placementId, paging);
        List<Equipment> equipment = pageEquipment.getContent();
        PaginationMap<Equipment> paginationMap = new PaginationMap<>(pageEquipment, equipment);
        return paginationMap.getPaginatedMap();
    }

    public Map<String, Object> getEquipmentBySubcategoryId(Long subcategoryId, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Equipment> pageEquipment = equipmentRepository.findBySubcategoryId(subcategoryId, paging);
        List<Equipment> equipment = pageEquipment.getContent();
        PaginationMap<Equipment> paginationMap = new PaginationMap<>(pageEquipment, equipment);
        return paginationMap.getPaginatedMap();
    }

    public void createEquipment(Equipment equipment) {
        equipmentRepository.save(equipment);
    }

    public void updateEquipmentById(Long id, Equipment equipment)
            throws ResourceNotFoundException {
        Equipment equipmentToUpdate = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment with id: " + id + " not found"));

        equipmentToUpdate.setInventoryNumber(equipment.getInventoryNumber());
        equipmentToUpdate.setName(equipmentToUpdate.getName());
        equipmentToUpdate.setImageData(equipment.getImageData());
        equipmentToUpdate.setDescription(equipment.getDescription());
        equipmentToUpdate.setCommissioningDate(equipment.getCommissioningDate());
        equipmentToUpdate.setCommissioningActNumber(equipment.getCommissioningActNumber());
        equipmentToUpdate.setDecommissioningDate(equipment.getDecommissioningDate());
        equipmentToUpdate.setDecommissioningActNumber(equipment.getDecommissioningActNumber());

        equipmentRepository.save(equipmentToUpdate);
    }

    public void deleteEquipmentById(Long id)
        throws ResourceNotFoundException, ResourceHasDependenciesException {
        Equipment equipmentToDelete = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment with id: " + id + " not found"));
        if (!equipmentToDelete.getRents().isEmpty()) {
            throw new ResourceHasDependenciesException("Equipment with id: " + id + " has relations with rents");
        }
        equipmentRepository.deleteById(id);
    }
}

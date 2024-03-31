package com.vlsu.inventory.service;

import com.vlsu.inventory.model.Rent;
import com.vlsu.inventory.repository.EquipmentRepository;
import com.vlsu.inventory.repository.RentRepository;
import com.vlsu.inventory.util.PaginationMap;
import com.vlsu.inventory.util.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class RentService {
    private final RentRepository rentRepository;

    private final EquipmentRepository equipmentRepository;

    public RentService(RentRepository rentRepository, EquipmentRepository equipmentRepository) {
        this.rentRepository = rentRepository;
        this.equipmentRepository = equipmentRepository;
    }

    public Map<String, Object> getRentsByCreateDatePageable(LocalDate date, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Rent> pageRents = rentRepository.findByCreateDate(date, paging);
        List<Rent> rents = pageRents.getContent();
        PaginationMap<Rent> paginationMap = new PaginationMap<>(pageRents, rents);
        return paginationMap.getPaginatedMap();
    }

    public Map<String, Object> getRentsByEndDatePageable(LocalDate date, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Rent> pageRents = rentRepository.findByEndDate(date, paging);
        List<Rent> rents = pageRents.getContent();
        PaginationMap<Rent> paginationMap = new PaginationMap<>(pageRents, rents);
        return paginationMap.getPaginatedMap();
    }

    public Map<String, Object> getRentsByCreateDateBetweenPageable(LocalDate fromDate, LocalDate toDate, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Rent> pageRents = rentRepository.findByCreateDateBetween(fromDate, toDate, paging);
        List<Rent> rents = pageRents.getContent();
        PaginationMap<Rent> paginationMap = new PaginationMap<>(pageRents, rents);
        return paginationMap.getPaginatedMap();
    }

    public Map<String, Object> getRentsByEndDateBetweenPageable(LocalDate fromDate, LocalDate toDate, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Rent> pageRents = rentRepository.findByEndDateBetween(fromDate, toDate, paging);
        List<Rent> rents = pageRents.getContent();
        PaginationMap<Rent> paginationMap = new PaginationMap<>(pageRents, rents);
        return paginationMap.getPaginatedMap();
    }

    public Map<String, Object> getRentsByCreateDateBeforePageable(LocalDate date, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Rent> pageRents = rentRepository.findByCreateDateBefore(date, paging);
        List<Rent> rents = pageRents.getContent();
        PaginationMap<Rent> paginationMap = new PaginationMap<>(pageRents, rents);
        return paginationMap.getPaginatedMap();
    }

    public Map<String, Object> getRentsByCreateDateAfterPageable(LocalDate date, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Rent> pageRents = rentRepository.findByCreateDateAfter(date, paging);
        List<Rent> rents = pageRents.getContent();
        PaginationMap<Rent> paginationMap = new PaginationMap<>(pageRents, rents);
        return paginationMap.getPaginatedMap();
    }

    public Map<String, Object> getRentsByEndDateBeforePageable(LocalDate date, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Rent> pageRents = rentRepository.findByEndDateBefore(date, paging);
        List<Rent> rents = pageRents.getContent();
        PaginationMap<Rent> paginationMap = new PaginationMap<>(pageRents, rents);
        return paginationMap.getPaginatedMap();
    }

    public Map<String, Object> getRentsByEndDateAfterPageable(LocalDate date, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Rent> pageRents = rentRepository.findByEndDateAfter(date, paging);
        List<Rent> rents = pageRents.getContent();
        PaginationMap<Rent> paginationMap = new PaginationMap<>(pageRents, rents);
        return paginationMap.getPaginatedMap();
    }


    public Map<String, Object> getRentsByEquipmentIdPageable(Long equipmentId, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Rent> pageRents = rentRepository.findByEquipmentId(equipmentId, paging);
        List<Rent> rents = pageRents.getContent();
        PaginationMap<Rent> paginationMap = new PaginationMap<>(pageRents, rents);
        return paginationMap.getPaginatedMap();
    }


    public Map<String, Object> getRentsByPlacementIdPageable(Long placementId, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Rent> pageRents = rentRepository.findByPlacementId(placementId, paging);
        List<Rent> rents = pageRents.getContent();
        PaginationMap<Rent> paginationMap = new PaginationMap<>(pageRents, rents);
        return paginationMap.getPaginatedMap();
    }

    public Map<String, Object> getRentsByResponsibleIdPageable(Long responsibleId, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Rent> pageRents = rentRepository.findByResponsibleId(responsibleId, paging);
        List<Rent> rents = pageRents.getContent();
        PaginationMap<Rent> paginationMap = new PaginationMap<>(pageRents, rents);
        return paginationMap.getPaginatedMap();
    }

    public void createRent(Rent rent) {
        rentRepository.save(rent);
    }

    public void updateRentById(Long id, Rent rent)
            throws ResourceNotFoundException {
        Rent rentToUpdate = rentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rent with id: " + id + " not found"));
        // the createDate field is not updated because it can be abused
        rentToUpdate.setEndDate(rent.getEndDate());
        rentToUpdate.setDescription(rentToUpdate.getDescription());
        rentRepository.save(rentToUpdate);
    }

    public void deleteRentById(Long id)
        throws ResourceNotFoundException {
        Rent rentToDelete = rentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rent with id: " + id + " not found"));
        rentRepository.deleteById(id);
    }

    public void deleteRentByEquipmentId(Long equipmentId)
            throws ResourceNotFoundException {
        if (!equipmentRepository.existsById(equipmentId)) {
            throw new ResourceNotFoundException("Equipment with id: " + equipmentId + " not found");
        }
        rentRepository.deleteByEquipmentId(equipmentId);
    }
}


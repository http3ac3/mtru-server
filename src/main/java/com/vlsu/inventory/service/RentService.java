package com.vlsu.inventory.service;

import com.vlsu.inventory.model.*;
import com.vlsu.inventory.repository.*;
import com.vlsu.inventory.security.UserDetailsImpl;
import com.vlsu.inventory.util.PaginationMap;
import com.vlsu.inventory.util.exception.ActionNotAllowedException;
import com.vlsu.inventory.util.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.vlsu.inventory.repository.RentRepository.*;

@Service
public class RentService {
    private final RentRepository rentRepository;
    private final EquipmentRepository equipmentRepository;
    private final ResponsibleRepository responsibleRepository;
    private final PlacementRepository placementRepository;
    private final UserRepository userRepository;

    public RentService(RentRepository rentRepository, EquipmentRepository equipmentRepository, ResponsibleRepository responsibleRepository, PlacementRepository placementRepository, UserRepository userRepository) {
        this.rentRepository = rentRepository;
        this.equipmentRepository = equipmentRepository;
        this.responsibleRepository = responsibleRepository;
        this.placementRepository = placementRepository;
        this.userRepository = userRepository;
    }

    public Rent getRentById(Long id) throws ResourceNotFoundException {
        return rentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Rent with id: " + id + " not found"));
    }

    public List<Rent> getAllRentsByParams(
            LocalDateTime createDateTimeFrom,
            LocalDateTime createDateTimeTo,
            LocalDateTime endDateTimeFrom,
            LocalDateTime endDateTimeTo,
            Boolean isClosed) throws ResourceNotFoundException {
        Specification<Rent> filter = (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("id"), 0);
        if (isClosed != null)
            filter = (isClosed) ? filter.and(endDateIsNotNull()) : filter.and(endDateIsNull());
        if (createDateTimeFrom != null)
            filter = filter.and(createDateTimeFrom(createDateTimeFrom));
        if (createDateTimeTo != null)
            filter = filter.and(createDateTimeTo(createDateTimeTo));
        if (endDateTimeFrom != null)
            filter = filter.and(endDateTimeFrom(endDateTimeFrom));
        if (endDateTimeTo != null)
            filter = filter.and(endDateTimeTo(endDateTimeTo));
        List<Rent> rents = rentRepository.findAll(filter);
        if (rents.isEmpty()) throw new ResourceNotFoundException("Nothing found");
        return rents;
    }

    public List<Rent> getRentsByEquipmentId(Long equipmentId) throws ResourceNotFoundException {
        if (!equipmentRepository.existsById(equipmentId))
            throw new ResourceNotFoundException("Equipment with id '" + equipmentId + "' not found");
        List<Rent> rents = rentRepository.findByEquipmentId(equipmentId);
        if (rents.isEmpty()) throw new ResourceNotFoundException("Nothing found");
        return rents;
    }

    public List<Rent> getRentsByPlacementId(Long placementId) throws ResourceNotFoundException {
        if (!placementRepository.existsById(placementId))
            throw new ResourceNotFoundException("Placement with id '" + placementId + "' not found");
        List<Rent> rents = rentRepository.findByPlacementId(placementId);
        if (rents.isEmpty()) throw new ResourceNotFoundException("Nothing was found");
        return rents;
    }

    public List<Rent> getRentsByResponsibleId(Long responsibleId) throws ResourceNotFoundException {
        if (!responsibleRepository.existsById(responsibleId))
            throw new ResourceNotFoundException("Responsible with id '" + responsibleId + "' not found");
        List<Rent> rents = rentRepository.findByResponsibleId(responsibleId);
        if (rents.isEmpty()) throw new ResourceNotFoundException("Nothing was found");
        return rents;
    }

    public List<Rent> getRentsByUser(UserDetailsImpl principal, Boolean isClosed) throws ResourceNotFoundException {
        Responsible responsible = userRepository.findByUsername(principal.getUsername()).get().getResponsible();
        List<Rent> rents = rentRepository.findByResponsibleId(responsible.getId());
        if (rents.isEmpty()) throw new ResourceNotFoundException("Nothing was found");

        if (isClosed)
            rents = rents.stream().filter(r -> r.getEndDate() != null).collect(Collectors.toList());
        else
            rents = rents.stream().filter(r -> r.getEndDate() == null).collect(Collectors.toList());

        return rents;
    }

    public void createRent(Long equipmentId, Long placementId, UserDetailsImpl principal, Rent rent)
            throws ResourceNotFoundException, ActionNotAllowedException {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                        .orElseThrow(() -> new ResourceNotFoundException("Equipment with id '" + equipmentId + "' not found"));
        if (equipment.isAlreadyRented())
            throw new ActionNotAllowedException("Equipment '" + equipment.getName() + "' is already rented");
        User user = userRepository.findByUsername(principal.getUsername()).get();
        Responsible responsible = user.getResponsible();
        Placement placement = placementRepository.findById(placementId)
                        .orElseThrow(() -> new ResourceNotFoundException("Placement with id '" + placementId + "' not found"));
        rent.setCreateDate(LocalDateTime.now());
        rent.setEquipment(equipment);
        rent.setResponsible(responsible);
        rent.setPlacement(placement);
        rentRepository.save(rent);
    }

    public void closeRentById(Long id) throws ResourceNotFoundException {
        Rent rentToUpdate = rentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rent with id '" + id + "' not found"));
        rentToUpdate.setEndDate(LocalDateTime.now());
        rentRepository.save(rentToUpdate);
    }

    public void deleteRentById(Long id) throws ResourceNotFoundException, ActionNotAllowedException {
        Rent rentToDelete = rentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rent with id: " + id + " not found"));
        if (rentToDelete.getEndDate() == null)
            throw new ActionNotAllowedException("Rent isn't close");
        rentRepository.deleteById(id);
    }

    public void deleteRentByEquipmentId(Long equipmentId)
            throws ResourceNotFoundException, ActionNotAllowedException {
        if (!equipmentRepository.existsById(equipmentId)) {
            throw new ResourceNotFoundException("Equipment with id: " + equipmentId + " not found");
        }
        if (rentRepository.findByEquipmentId(equipmentId).stream().anyMatch(r -> r.getEndDate() == null))
            throw new ActionNotAllowedException("There is the rent, that doesn't closed");
        rentRepository.deleteByEquipmentId(equipmentId);
    }

    public static Map<String, Object> getRentsPageable(List<Rent> rents, int page, int size) throws Exception {
        page = page - 1;
        if (page < 0) throw new Exception("Страница не может быть меньше или равна 0");
        Pageable paging = PageRequest.of(page, size);
        int start = (int) paging.getOffset();
        int end = Math.min((start + paging.getPageSize()), rents.size());
        List<Rent> pageContent = rents.subList(start, end);
        Page<Rent> pageRents = new PageImpl<>(pageContent, paging, rents.size());
        PaginationMap<Rent> paginationMap = new PaginationMap<>(pageRents, "rents");
        return paginationMap.getPaginatedMap();
    }
}


package com.vlsu.inventory.service;

import com.vlsu.inventory.model.*;
import com.vlsu.inventory.repository.*;
import com.vlsu.inventory.security.UserDetailsImpl;
import com.vlsu.inventory.util.PaginationMap;
import com.vlsu.inventory.util.exception.ActionNotAllowedException;
import com.vlsu.inventory.util.exception.ResourceHasDependenciesException;
import com.vlsu.inventory.util.exception.ResourceNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.vlsu.inventory.repository.EquipmentRepository.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class EquipmentService {

    EquipmentRepository equipmentRepository;
    ResponsibleRepository responsibleRepository;
    PlacementRepository placementRepository;
    SubcategoryRepository subcategoryRepository;
    UserRepository userRepository;

    public List<Equipment> getAllEquipmentByParams(
            String inventoryNumber, String name, BigDecimal initialCostFrom, BigDecimal initialCostTo,
            LocalDate commissioningDateFrom, LocalDate commissioningDateTo,
            LocalDate decommissioningDateFrom, LocalDate decommissioningDateTo,
            String commissioningActNumber, String decommissioningActNumber,
            Long subcategoryId, Long responsibleId, Long placementId) {
        Specification<Equipment> filter = (equipment, query, cb) -> cb.greaterThan(equipment.get("id"), 0);
        if (inventoryNumber != null)
            filter = filter.and(inventoryNumberStartsWith(inventoryNumber));
        if (name != null)
            filter = filter.and(nameStartsWith(name));
        if (initialCostFrom != null)
            filter = filter.and(initialCostFrom(initialCostFrom));
        if (initialCostTo != null)
            filter = filter.and(initialCostTo(initialCostTo));
        if (commissioningDateFrom != null)
            filter = filter.and(commissioningDateFrom(commissioningDateFrom));
        if (commissioningDateTo != null)
            filter = filter.and(commissioningDateTo(commissioningDateTo));
        if (decommissioningDateFrom != null)
            filter = filter.and(decommissioningDateFrom(decommissioningDateFrom));
        if (decommissioningDateTo != null)
            filter = filter.and(decommissioningDateTo(decommissioningDateTo));
        if (commissioningActNumber != null)
            filter = filter.and(commissioningActNumberLike(commissioningActNumber));
        if (decommissioningActNumber != null)
            filter = filter.and(decommissioningActNumberLike(decommissioningActNumber));
        if (subcategoryId != null)
            filter = filter.and(subcategoryIdEquals(subcategoryId));
        if (responsibleId != null)
            filter = filter.and(responsibleIdEquals(responsibleId));
        if (placementId != null)
            filter = filter.and(placementIdEquals(placementId));
        return equipmentRepository.findAll(filter);
    }

    public Equipment getEquipmentById(Long id) throws ResourceNotFoundException {
        return equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Equipment with id: " + id + " not found"));
    }

    public List<Equipment> getEquipmentByInventoryNumber(String inventoryNumber)
            throws ResourceNotFoundException {
        List<Equipment> equipment = equipmentRepository.findByInventoryNumberStartingWith(inventoryNumber);
        if (equipment.isEmpty()) {
            throw new ResourceNotFoundException("Equipment with inventory number '" + inventoryNumber +"' not found");
        }
        return equipment;
    }

    public List<Equipment> getEquipmentBySubcategoryId(Long subcategoryId) throws ResourceNotFoundException {
        if (!subcategoryRepository.existsById(subcategoryId)) {
            throw new ResourceNotFoundException("Subcategory with id '" + subcategoryId + "' not found");
        }
        List<Equipment> equipment = equipmentRepository.findBySubcategoryId(subcategoryId);
        if (equipment.isEmpty()) {
            throw new ResourceNotFoundException("Equipment with subcategory id '" + subcategoryId + "' not found");
        }
        return equipment;
    }

    public List<Equipment> getEquipmentByResponsibleId(Long responsibleId) throws ResourceNotFoundException {
        if (!responsibleRepository.existsById(responsibleId)) {
            throw new ResourceNotFoundException("Responsible with id '" + responsibleId + "' not found");
        }
        List<Equipment> equipment = equipmentRepository.findByResponsibleId(responsibleId);
        if (equipment.isEmpty()) {
            throw new ResourceNotFoundException("Equipment with responsible id '" + responsibleId + "' not found");
        }
        return equipment;
    }


    public List<Equipment> getEquipmentByPlacementId(Long placementId) throws ResourceNotFoundException {
        if (!placementRepository.existsById(placementId)) {
            throw new ResourceNotFoundException("Placement with id '" + placementId + "' not found");
        }
        List<Equipment> equipment = equipmentRepository.findByPlacementId(placementId);
        if (equipment.isEmpty()) {
            throw new ResourceNotFoundException("Equipment with placement id '" + placementId + "' not found");
        }
        return equipment;
    }

    public void createEquipment(Long subcategoryId, Long responsibleId, Long placementId, Equipment equipment,
                                User principal)
            throws ResourceNotFoundException, ActionNotAllowedException {
        User user = userRepository.findByUsername(principal.getUsername()).get();
        Responsible responsible = user.getResponsible();
        if (user.isAdmin()) {
            responsible = responsibleRepository.findById(responsibleId)
                    .orElseThrow(() -> new ResourceNotFoundException("Responsible with id '" + responsibleId + "' not found"));
            if (!responsible.isFinanciallyResponsible()) {
                throw new ActionNotAllowedException("Responsible " + responsible.getLastName() + " "
                        + responsible.getFirstName() + " can't be financially responsible for equipment");
            }
        }
        Placement placement = placementRepository.findById(placementId)
                .orElseThrow(() -> new ResourceNotFoundException("Placement with id '" + placementId + "' not found"));
        Subcategory subcategory = subcategoryRepository.findById(subcategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Subcategory with id '" + subcategoryId + "' not found"));
        equipment.setResponsible(responsible);
        equipment.setPlacement(placement);
        equipment.setSubcategory(subcategory);
        equipmentRepository.save(equipment);
    }

    public void updateEquipmentById(
            Long id, Long subcategoryId, Long responsibleId,
            Long placementId, Equipment equipmentRequest,
            User principal)
            throws ResourceNotFoundException, ActionNotAllowedException {
        Equipment equipment = equipmentRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Equipment with id '" + id + "' not found"));
        User user = userRepository.findByUsername(principal.getUsername()).get();
        Responsible responsible = user.getResponsible();
        if (!Objects.equals(equipmentRequest.getResponsible().getId(), responsible.getId()) && !user.isAdmin()) {
            throw new ActionNotAllowedException("Equipment with inventory number '" + equipment.getInventoryNumber() +
                    " doesn't belong to " + responsible.getLastName()  + " " + responsible.getFirstName());
        }
        if (user.isAdmin()) {
            responsible = responsibleRepository.findById(responsibleId)
                    .orElseThrow(() -> new ResourceNotFoundException("Responsible with id '" + responsibleId + "' not found"));

            if (!responsible.isFinanciallyResponsible()) {
                throw new ActionNotAllowedException("Responsible " + responsible.getLastName() + " "
                        + responsible.getFirstName() + " can't be financially responsible for equipment");
            }
        }
        Placement placement = placementRepository.findById(placementId)
                        .orElseThrow(() -> new ResourceNotFoundException("Placement with id '" + placementId + "' not found"));
        Subcategory subcategory = subcategoryRepository.findById(subcategoryId)
                        .orElseThrow(() -> new ResourceNotFoundException("Subcategory with id '" + subcategoryId + "' not found"));

        equipment = new Equipment(equipmentRequest.getInventoryNumber(),  equipmentRequest.getName(),
                equipmentRequest.getImageData(), equipmentRequest.getDescription(), equipmentRequest.getInitialCost(),
                equipmentRequest.getCommissioningDate(), equipmentRequest.getCommissioningActNumber(),
                equipmentRequest.getDecommissioningDate(), equipmentRequest.getDecommissioningActNumber());
        equipment.setId(id);
        equipment.setResponsible(responsible);
        equipment.setPlacement(placement);
        equipment.setSubcategory(subcategory);

        equipmentRepository.save(equipment);
    }

    public void deleteEquipmentById(Long id, User principal)
            throws ResourceNotFoundException, ResourceHasDependenciesException, ActionNotAllowedException {
        User user = userRepository.findByUsername(principal.getUsername()).get();
        Responsible responsible = user.getResponsible();
        Equipment equipmentToDelete = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment with id: " + id + " not found"));
        if (!user.isAdmin() && !Objects.equals(equipmentToDelete.getResponsible().getId(), responsible.getId())) {
            throw new ActionNotAllowedException("Equipment doesn't belong to responsible "
                    + responsible.getLastName() + " " + responsible.getLastName());
        }
        if (!equipmentToDelete.getRents().isEmpty()) {
            throw new ResourceHasDependenciesException("Equipment with id: " + id + " has relations with rents");
        }
        equipmentRepository.deleteById(id);
    }

    public static Map<String, Object> getEquipmentByPage(List<Equipment> equipment, int page, int size)
            throws Exception {
        page = page - 1;
        if (page < 0) {
            throw new Exception("Страница не может быть меньше или равна 0");
        }
        Pageable paging = PageRequest.of(page, size);
        int start = (int) paging.getOffset();
        int end = Math.min((start + paging.getPageSize()), equipment.size());
        List<Equipment> pageContent = equipment.subList(start, end);
        Page<Equipment> pageResponsible = new PageImpl<>(pageContent, paging, equipment.size());
        PaginationMap<Equipment> paginationMap = new PaginationMap<>(pageResponsible, "equipment");
        return paginationMap.getPaginatedMap();
    }
}

package com.vlsu.inventory.service;

import com.vlsu.inventory.dto.model.EquipmentDto;
import com.vlsu.inventory.model.*;
import com.vlsu.inventory.repository.*;
import com.vlsu.inventory.util.PaginationMap;
import com.vlsu.inventory.util.exception.ActionNotAllowedException;
import com.vlsu.inventory.util.exception.ResourceHasDependenciesException;
import com.vlsu.inventory.util.exception.ResourceNotFoundException;
import com.vlsu.inventory.util.mapping.EquipmentMappingUtils;
import com.vlsu.inventory.util.mapping.PlacementMappingUtils;
import com.vlsu.inventory.util.mapping.ResponsibleMappingUtils;
import com.vlsu.inventory.util.mapping.SubcategoryMappingUtils;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    ResponsibleService responsibleService;
    PlacementService placementService;
    SubcategoryService subcategoryService;
    UserRepository userRepository;
    ImageService imageService;
    RentRepository rentRepository;
    ResponsibleRepository responsibleRepository;

    public List<EquipmentDto.Response.Default> getAllByParams(
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
        List<Equipment> equipment = equipmentRepository.findAll(filter);



        return equipment.stream().map(EquipmentMappingUtils::toDto).toList();
    }

    public EquipmentDto.Response.Default getById(Long id) throws ResourceNotFoundException {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Equipment with id: " + id + " not found"));
        return EquipmentMappingUtils.toDto(equipment);
    }

    public String getBase64ImageById(Long id) throws IOException {
        String imagePath = equipmentRepository.findImageDataById(id);
        return imageService.getImageBase64String(imagePath);
    }

    // TODO Simplify is admin user check and setting responsible to equipment
    public void create(EquipmentDto.Request.Create request, User principal) throws Exception {
        Placement placement = PlacementMappingUtils.fromDto(placementService.getById(request.getPlacementId()));
        Subcategory subcategory = SubcategoryMappingUtils.fromDto(subcategoryService.getById(request.getSubcategoryId()));

        Responsible responsible = userRepository.findByUsername(principal.getUsername()).get().getResponsible();
        Responsible requestResponsible = responsibleRepository.findById(request.getResponsibleId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Responsible with id '" + request.getResponsibleId() + "' not found"));

        if (!principal.isAdmin() && !Objects.equals(
                requestResponsible.getDepartment().getId(),
                responsible.getDepartment().getId())) {
            throw new ActionNotAllowedException(
                    "Responsible " + requestResponsible.getLastName() + " " +
                    requestResponsible.getFirstName() + " " +
                    " can't have responsibility to equipment with inventory number '" +
                    request.getInventoryNumber() + " , because he doesn't belong to department" +
                    responsible.getDepartment());
        }

        Equipment create = EquipmentMappingUtils.fromDto(request);
        create.setResponsible(requestResponsible);
        create.setPlacement(placement);
        create.setSubcategory(subcategory);
        Equipment saved = equipmentRepository.save(create);

        if (request.getImage() != null) {
            saveImage(request.getImage(), saved.getId());
        }
    }

    public void update(EquipmentDto.Request.Update request, User principal) throws Exception {
        Equipment equipment = equipmentRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment with id '" + request.getId() + "' not found"));

        String imageData = equipment.getImageData();
        Placement placement = PlacementMappingUtils.fromDto(placementService.getById(request.getPlacementId()));
        Subcategory subcategory = SubcategoryMappingUtils.fromDto(subcategoryService.getById(request.getSubcategoryId()));

        Responsible responsible = userRepository.findByUsername(principal.getUsername()).get().getResponsible();
        Responsible requestResponsible = responsibleRepository.findById(request.getResponsibleId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Responsible with id '" + request.getResponsibleId() + "' not found"));

        if (!principal.isAdmin() && !Objects.equals(
                requestResponsible.getDepartment().getId(),
                responsible.getDepartment().getId())
        ) {
            throw new ActionNotAllowedException("Equipment with inventory number '" + request.getInventoryNumber() +
                    " doesn't belong to " + responsible.getLastName()  + " " + responsible.getFirstName());
        }

        Equipment update = EquipmentMappingUtils.fromDto(request);

        update.setResponsible(requestResponsible);
        update.setPlacement(placement);
        update.setSubcategory(subcategory);
        update.setImageData(imageData);
        equipmentRepository.save(update);

        if (request.getImage() != null) {
            if (imageData != null) {
                imageService.deleteImage(imageData);
            }
            saveImage(request.getImage(), request.getId());
        }
    }

    @Transactional
    public void delete(Long id, User principal)
            throws ResourceNotFoundException, ResourceHasDependenciesException, ActionNotAllowedException {
        Responsible responsible = userRepository.findByUsername(principal.getUsername()).get().getResponsible();
        Equipment equipmentToDelete = equipmentRepository
                .findWithRentsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment with id: " + id + " not found"));

        if (!principal.isAdmin() && !Objects.equals(
                equipmentToDelete.getResponsible().getDepartment().getId(),
                responsible.getDepartment().getId())
        ) {
            throw new ActionNotAllowedException("Equipment doesn't belong to responsible "
                    + responsible.getLastName() + " " + responsible.getLastName());
        }

        rentRepository.deleteByEquipmentId(id);

        if (equipmentToDelete.getImageData() != null)
            imageService.deleteImage(equipmentToDelete.getImageData());

        equipmentRepository.deleteById(id);
    }

    private void saveImage(MultipartFile image, Long id) throws Exception {
        String path = imageService.save(image, id);
        try {
            equipmentRepository.updateImageReference(id, path);
        } catch (Exception ignored) { }
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

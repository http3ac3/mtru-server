package com.vlsu.inventory.service;

import com.vlsu.inventory.dto.model.RentDto;
import com.vlsu.inventory.model.*;
import com.vlsu.inventory.repository.*;
import com.vlsu.inventory.util.PaginationMap;
import com.vlsu.inventory.util.exception.ActionNotAllowedException;
import com.vlsu.inventory.util.exception.ResourceNotFoundException;
import com.vlsu.inventory.util.mapping.RentMappingUtils;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.vlsu.inventory.repository.specification.RentSpecification.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class RentService {

    RentRepository rentRepository;
    EquipmentRepository equipmentRepository;
    ResponsibleService responsibleService;
    PlacementService placementService;
    UserRepository userRepository;

    public RentDto.Response.Default getById(Long id) throws ResourceNotFoundException {
        Rent rent = rentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Rent with id: " + id + " not found"));

        return RentMappingUtils.toDto(rent);
    }

    public List<RentDto.Response.Default> getAllByParams(
            LocalDateTime createDateTimeFrom,
            LocalDateTime createDateTimeTo,
            LocalDateTime endDateTimeFrom,
            LocalDateTime endDateTimeTo,
            Boolean isClosed,
            Long equipmentId,
            Long responsibleId,
            Long placementId) {
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
        if (equipmentId != null)
            filter = filter.and(equipmentIdEquals(equipmentId));
        if (responsibleId != null)
            filter = filter.and(responsibleIdEquals(responsibleId));
        if (placementId != null)
            filter = filter.and(placementsIdEquals(placementId));

        List<Rent> rents = rentRepository.findAll(filter);
        return rents.stream().map(RentMappingUtils::toDto).toList();
    }

    public List<RentDto.Response.UserUnclosed> getUnclosedByUser(User principal) {
        Responsible responsible = userRepository.findByUsername(principal.getUsername()).get().getResponsible();
        List<Rent> rents = rentRepository.findUnclosedByResponsibleId(responsible.getId());
        return rents.stream().map(RentMappingUtils::toDtoUserUnclosed).toList();
    }

    @Transactional
    public RentDto.Request.Create create(RentDto.Request.Create request, User principal)
            throws ResourceNotFoundException, ActionNotAllowedException {
        Equipment equipment = equipmentRepository.findById(request.getEquipment().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Equipment with id '" +
                                request.getEquipment().getId() + "' not found"));
        placementService.getById(request.getPlacement().getId());
        if (equipment.getUnclosedRent() != null)
            throw new ActionNotAllowedException("Equipment '" + equipment.getName() + "' is already rented");
        else if (equipment.getDecommissioningDate() != null)
            throw new ActionNotAllowedException("Equipment '" + equipment.getName() + "' is decommissioned");

        User user = userRepository.findByUsername(principal.getUsername()).get();
        Responsible responsible = user.getResponsible();
        Rent create = RentMappingUtils.fromDto(request);
        create.setCreateDateTime(LocalDateTime.now());
        create.setResponsible(responsible);
        rentRepository.save(create);
        return request;
    }

    // TODO Do impossible to close others rent by user
    public void closeRent(Long id) throws ResourceNotFoundException {
        Rent update = rentRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rent with id '" + id + "' not found"));
        update.setEndDateTime(LocalDateTime.now());
        rentRepository.save(update);
    }

    public void delete(Long id) throws ResourceNotFoundException, ActionNotAllowedException {
        Rent rent = rentRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rent with id '" + id + "' not found"));
        if (rent.getEndDateTime() == null)
            throw new ActionNotAllowedException("Rent isn't close");
        rentRepository.deleteById(id);
    }

    public void deleteRentByEquipmentId(Long equipmentId)
            throws ResourceNotFoundException, ActionNotAllowedException {
        if (!equipmentRepository.existsById(equipmentId)) {
            throw new ResourceNotFoundException("Equipment with id '" + equipmentId + "' not found");
        }
        if (rentRepository.findByEquipmentId(equipmentId).stream().anyMatch(r -> r.getEndDateTime() == null))
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


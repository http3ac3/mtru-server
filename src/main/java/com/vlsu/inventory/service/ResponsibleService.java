package com.vlsu.inventory.service;

import com.vlsu.inventory.dto.model.ResponsibleDto;
import com.vlsu.inventory.model.Responsible;
import com.vlsu.inventory.model.User;
import com.vlsu.inventory.repository.RentRepository;
import com.vlsu.inventory.repository.ResponsibleRepository;
import com.vlsu.inventory.repository.UserRepository;
import com.vlsu.inventory.util.PaginationMap;
import com.vlsu.inventory.util.exception.ResourceHasDependenciesException;
import com.vlsu.inventory.util.exception.ResourceNotFoundException;
import com.vlsu.inventory.util.mapping.ResponsibleMappingUtils;
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

import static com.vlsu.inventory.repository.specification.ResponsibleSpecification.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ResponsibleService {

    ResponsibleRepository responsibleRepository;
    DepartmentService departmentService;
    UserRepository userRepository;
    RentRepository rentRepository;
    UserService userService;

    // TODO Fix fetching User and Role info with Responsible data
    public List<ResponsibleDto.Response.Default> getAll(
            String firstName,
            String lastName,
            Boolean isFinanciallyResponsible,
            Long departmentId) {
        Specification<Responsible> filter = (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("id"), 0);
        if (firstName != null) {
            filter = filter.and(firstNameLike(firstName));
        }
        if (lastName != null) {
            filter = filter.and(lastNameLike(lastName));
        }
        if (isFinanciallyResponsible != null) {
            filter = filter.and(isFinanciallyResponsible(isFinanciallyResponsible));
        }
        if (departmentId != null) {
            filter = filter.and(departmentIdEquals(departmentId));
        }
        List<Responsible> responsibleList = responsibleRepository.findAll(filter);
        return responsibleList.stream().map(ResponsibleMappingUtils::toDto).toList();
    }

    public ResponsibleDto.Response.Default getById(Long id) throws ResourceNotFoundException {
        Responsible responsible = responsibleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Responsible with id '" + id + "' not found"));
        return ResponsibleMappingUtils.toDto(responsible);
    }

    public Responsible getByPrincipal(User principal) {
        return userRepository.findByUsername(principal.getUsername()).get().getResponsible();
    }

    public Responsible create(ResponsibleDto.Request.Create request) throws ResourceNotFoundException {
        departmentService.getById(request.getDepartment().getId());
        Responsible create = ResponsibleMappingUtils.fromDto(request);
        System.out.println(create.getPhoneNumber());
        return responsibleRepository.save(create);
    }

    public Responsible update(ResponsibleDto.Request.Update request) throws ResourceNotFoundException {
        departmentService.getById(request.getDepartment().getId());
        if (!responsibleRepository.existsById(request.getId())) {
            throw new ResourceNotFoundException("Responsible with id '" + request.getId() + "' not found");
        }
        Responsible update = ResponsibleMappingUtils.fromDto(request);
        return responsibleRepository.save(update);
    }

    // TODO Check only unclosed Rents references for deleting Responsible
    @Transactional
    public void delete(Long id) throws ResourceNotFoundException, ResourceHasDependenciesException {
        Responsible responsible = responsibleRepository
                .findWithEquipmentById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Responsible with id '" + id + "' not found"));
        if (!responsible.getEquipment().isEmpty()) {
            throw new ResourceHasDependenciesException("Responsible with id '" + id + "' has relations with equipment");
        }
        rentRepository.deleteByResponsibleId(id);
        userService.deleteUserById(responsible.getUser().getId());

        responsibleRepository.deleteById(id);
    }

    public static Map<String, Object> getResponsibleByPage(List<Responsible> responsible, int page, int size)
        throws Exception {
        if (page < 0) {
            throw new Exception("Страница не может быть меньше или равна 0");
        }
        Pageable paging = PageRequest.of(page, size);
        int start = (int) paging.getOffset();
        int end = Math.min((start + paging.getPageSize()), responsible.size());
        List<Responsible> pageContent = responsible.subList(start, end);
        Page<Responsible> pageResponsible = new PageImpl<>(pageContent, paging, responsible.size());
        PaginationMap<Responsible> paginationMap = new PaginationMap<>(pageResponsible, "responsible");
        return paginationMap.getPaginatedMap();
    }
}

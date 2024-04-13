package com.vlsu.inventory.service;

import com.vlsu.inventory.model.Department;
import com.vlsu.inventory.model.Equipment;
import com.vlsu.inventory.model.Responsible;
import com.vlsu.inventory.repository.DepartmentRepository;
import com.vlsu.inventory.repository.ResponsibleRepository;
import com.vlsu.inventory.util.PaginationMap;
import com.vlsu.inventory.util.exception.ResourceHasDependenciesException;
import com.vlsu.inventory.util.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ResponsibleService {
    private final ResponsibleRepository responsibleRepository;
    private final DepartmentRepository departmentRepository;

    public ResponsibleService(ResponsibleRepository responsibleRepository, DepartmentRepository departmentRepository) {
        this.responsibleRepository = responsibleRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<Responsible> getAllResponsible(
            String firstName,
            String lastName,
            Boolean isFinanciallyResponsible,
            Long departmentId) {
        Specification<Responsible> filter = (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("id"), 0);
        if (firstName != null) {
            filter = filter.and(ResponsibleRepository.firstNameLike(firstName));
        }
        if (lastName != null) {
            filter = filter.and(ResponsibleRepository.lastNameLike(lastName));
        }
        if (isFinanciallyResponsible != null) {
            filter = filter.and(ResponsibleRepository.isFinanciallyResponsible(isFinanciallyResponsible));
        }
        if (departmentId != null) {
            filter = filter.and(ResponsibleRepository.departmentIdEquals(departmentId));
        }
        return responsibleRepository.findAll(filter);
    }

    public Responsible getResponsibleById(Long id) throws ResourceNotFoundException {
        return responsibleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Responsible with id '" + id + "' not found"));
    }

    public List<Responsible> getResponsibleByDepartmentId(Long departmentId) throws ResourceNotFoundException {
        if (!departmentRepository.existsById(departmentId))
            throw new ResourceNotFoundException("Department with id '" + departmentId + "' not found");
        List<Responsible> responsible = responsibleRepository.findByDepartmentId(departmentId);
        if (responsible.isEmpty()) {
            throw new ResourceNotFoundException("Responsible with department id '" + departmentId + "' not found");
        }
        return responsible;
    }

    public void createResponsible(Long departmentId, Responsible responsibleRequest)
            throws ResourceNotFoundException {
            Responsible responsible = departmentRepository.findById(departmentId).map(department -> {
                responsibleRequest.setDepartment(department);
                return responsibleRepository.save(responsibleRequest);
            }).orElseThrow(() -> new ResourceNotFoundException("Department with id '" + departmentId + "' not found"));
        responsibleRepository.save(responsible);
    }

    public void updateResponsibleById(Long id, Long departmentId, Responsible responsibleRequest)
            throws ResourceNotFoundException {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department with id '" + departmentId + "' not found"));
        Responsible responsible = responsibleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Responsible with id '" + id + "' not found"));
        responsible.setFirstName(responsibleRequest.getFirstName());
        responsible.setLastName(responsibleRequest.getLastName());
        responsible.setPatronymic(responsibleRequest.getPatronymic());
        responsible.setPhoneNumber(responsibleRequest.getPhoneNumber());
        responsible.setFinanciallyResponsible(responsibleRequest.isFinanciallyResponsible());
        responsible.setPosition(responsibleRequest.getPosition());
        responsible.setDepartment(department);
        responsibleRepository.save(responsible);
    }

    public void deleteById(Long id)
            throws ResourceNotFoundException, ResourceHasDependenciesException {
        Responsible responsibleToDelete = responsibleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Responsible with id '" + id + "' not found"));
        if (!responsibleToDelete.getEquipment().isEmpty()) {
            throw new ResourceHasDependenciesException("Responsible with id '" + id + "' has relations with equipment");
        }
        else if (!responsibleToDelete.getRents().isEmpty()) {
            throw new ResourceHasDependenciesException("Responsible with id '" + id + "' has relations with rents");
        }
        else if (responsibleToDelete.getUser() != null) {
            throw new ResourceHasDependenciesException("Responsible with id '" + id + "' is associate with user");
        }
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

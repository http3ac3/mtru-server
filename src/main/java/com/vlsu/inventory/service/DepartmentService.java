package com.vlsu.inventory.service;

import com.vlsu.inventory.dto.model.DepartmentDto;
import com.vlsu.inventory.model.Department;
import com.vlsu.inventory.repository.DepartmentRepository;
import com.vlsu.inventory.util.exception.ResourceHasDependenciesException;
import com.vlsu.inventory.util.exception.ResourceNotFoundException;
import com.vlsu.inventory.util.mapping.DepartmentMappingUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class DepartmentService {

    DepartmentRepository departmentRepository;

    public List<DepartmentDto.Response.Default> getAll() {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream().map(DepartmentMappingUtils::toDto).collect(Collectors.toList());
    }

    public DepartmentDto.Response.Default getById(Long id) throws ResourceNotFoundException {
        Optional<Department> department = departmentRepository.findById(id);
        if (department.isEmpty()) {
            throw new ResourceNotFoundException("Department with id: " + id + " not found");
        }
        return DepartmentMappingUtils.toDto(department.get());
    }

    public void create(DepartmentDto.Request.Create request) {
        Department department = DepartmentMappingUtils.fromDto(request);
        departmentRepository.save(department);
    }

    public void update(DepartmentDto.Request.Update request) throws ResourceNotFoundException {
        Department department = departmentRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Department with id: " + request.getId() + " not found"));
        department.setName(request.getName());
        departmentRepository.save(department);
    }

    public void delete(Long id) throws ResourceNotFoundException, ResourceHasDependenciesException {
        Department department = departmentRepository.findWithResponsibleById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department with id: " + id + " not found"));
        if (!department.getResponsibles().isEmpty()) {
            throw new ResourceHasDependenciesException("Department with id: " + id + " has relations with responsibles");
        }
        departmentRepository.deleteById(id);
    }
}

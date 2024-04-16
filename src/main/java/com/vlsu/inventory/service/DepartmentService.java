package com.vlsu.inventory.service;

import com.vlsu.inventory.model.Department;
import com.vlsu.inventory.repository.DepartmentRepository;
import com.vlsu.inventory.util.exception.ResourceHasDependenciesException;
import com.vlsu.inventory.util.exception.ResourceNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class DepartmentService {

    DepartmentRepository departmentRepository;

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(Long id) throws ResourceNotFoundException {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department with id: " + id + " not found"));
    }
    public Department getDepartmentByName(String name) throws ResourceNotFoundException {
        return departmentRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Department with name: " + name + " not found"));
    }

    public void createDepartment(Department department) {
        departmentRepository.save(department);
    }

    public void updateDepartmentById(Long id, Department department) throws ResourceNotFoundException {
        Department departmentToUpdate = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department with id: " + id + " not found"));

        departmentToUpdate.setName(department.getName());
        departmentRepository.save(departmentToUpdate);
    }

    public void deleteDepartmentById(Long id) throws ResourceNotFoundException, ResourceHasDependenciesException {
        Department departmentToDelete = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department with id: " + id + " not found"));
        if (!departmentToDelete.getResponsibles().isEmpty()) {
            throw new ResourceHasDependenciesException("Department with id: " + id + " has relations with responsibles");
        }
        departmentRepository.deleteById(id);
    }
}

package com.vlsu.inventory.service;

import com.vlsu.inventory.model.Department;
import com.vlsu.inventory.repository.DepartmentRepository;
import com.vlsu.inventory.util.exception.ResourceHasDependenciesException;
import com.vlsu.inventory.util.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
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
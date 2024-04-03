package com.vlsu.inventory.service;

import com.vlsu.inventory.model.Department;
import com.vlsu.inventory.model.Responsible;
import com.vlsu.inventory.repository.DepartmentRepository;
import com.vlsu.inventory.repository.ResponsibleRepository;
import com.vlsu.inventory.util.exception.ResourceHasDependenciesException;
import com.vlsu.inventory.util.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponsibleService {
    private final ResponsibleRepository responsibleRepository;
    private final DepartmentRepository departmentRepository;

    public ResponsibleService(ResponsibleRepository responsibleRepository, DepartmentRepository departmentRepository) {
        this.responsibleRepository = responsibleRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<Responsible> getAllResponsible() {
        return responsibleRepository.findAll();
    }

    public Responsible getResponsibleById(Long id) throws ResourceNotFoundException {
        return responsibleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Responsible with id '" + id + "' not found"));
    }

    public List<Responsible> getResponsibleByFullName(String firstName, String lastName, String patronymic) {
        return responsibleRepository.findByFullName(firstName, lastName, patronymic);
    }

    public List<Responsible> getResponsibleByFinanciallyResponsibility(boolean isFinanciallyResponsible) {
        return responsibleRepository.findByFinanciallyResponsibility(isFinanciallyResponsible);
    }

    public List<Responsible> getResponsibleByDepartmentId(Long departmentId) throws ResourceNotFoundException {
        if (!departmentRepository.existsById(departmentId))
            throw new ResourceNotFoundException("Department with id '" + departmentId + "' not found");
        return responsibleRepository.findByDepartmentId(departmentId);
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
        else if (!responsibleToDelete.getUsers().isEmpty()) {
            throw new ResourceHasDependenciesException("Responsible with id '" + id + "' is associate with user");
        }
        responsibleRepository.deleteById(id);
    }
}

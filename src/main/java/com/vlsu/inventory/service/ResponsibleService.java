package com.vlsu.inventory.service;

import com.vlsu.inventory.model.Responsible;
import com.vlsu.inventory.repository.ResponsibleRepository;
import com.vlsu.inventory.util.exception.ResourceHasDependenciesException;
import com.vlsu.inventory.util.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponsibleService {
    private final ResponsibleRepository responsibleRepository;

    public ResponsibleService(ResponsibleRepository responsibleRepository) {
        this.responsibleRepository = responsibleRepository;
    }

    public List<Responsible> getAllResponsible() {
        return responsibleRepository.findAll();
    }

    public Responsible getResponsibleById(Long id) throws ResourceNotFoundException {
        return responsibleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Responsible with id: " + id + " not found"));
    }

    public List<Responsible> getResponsibleByFullName(String firstName, String lastName, String patronymic) {
        return responsibleRepository.findByFullName(firstName, lastName, patronymic);
    }

    public List<Responsible> getResponsibleByFinanciallyResponsibility(boolean isFinanciallyResponsible) {
        return responsibleRepository.findByFinanciallyResponsibility(isFinanciallyResponsible);
    }

    public List<Responsible> getResponsibleByDepartmentId(Long departmentId) {
        return responsibleRepository.findByDepartmentId(departmentId);
    }

    public void createResponsible(Responsible responsible) {
        responsibleRepository.save(responsible);
    }

    public void updateResponsibleById(Long id, Responsible responsible)
            throws ResourceNotFoundException
    {
        Responsible responsibleToUpdate = responsibleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Responsible with id: " + id + " not found"));
        responsibleToUpdate.setFirstName(responsible.getFirstName());
        responsibleToUpdate.setLastName(responsible.getLastName());
        responsibleToUpdate.setPatronymic(responsibleToUpdate.getPatronymic());
        responsibleToUpdate.setPhoneNumber(responsible.getPhoneNumber());
        responsibleToUpdate.setFinanciallyResponsible(responsible.isFinanciallyResponsible());
        responsibleToUpdate.setPosition(responsible.getPosition());

        responsibleRepository.save(responsibleToUpdate);
    }

    public void deleteById(Long id)
            throws ResourceNotFoundException, ResourceHasDependenciesException {
        Responsible responsibleToDelete = responsibleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Responsible with id: " + id + " not found"));
        if (!responsibleToDelete.getEquipment().isEmpty()) {
            throw new ResourceHasDependenciesException("Responsible with id: " + id + " has relations with equipment");
        }
        else if (!responsibleToDelete.getRents().isEmpty()) {
            throw new ResourceHasDependenciesException("Responsible with id: " + id + " has relations with rents");
        }
        else if (!responsibleToDelete.getUsers().isEmpty()) {
            throw new ResourceHasDependenciesException("Responsible with id: " + id + " is associate with user");
        }
        responsibleRepository.deleteById(id);
    }
}

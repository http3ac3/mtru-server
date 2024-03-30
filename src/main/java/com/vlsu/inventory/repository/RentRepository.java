package com.vlsu.inventory.repository;

import com.vlsu.inventory.model.Rent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RentRepository extends JpaRepository<Rent, Long> {
    List<Rent> findByCreateDate(LocalDate createDate);
    List<Rent> findByEndDate(LocalDate date);
    List<Rent> findByCreateDateBetween(LocalDate fromCreateDate, LocalDate toCreateDate);
    List<Rent> findByEndDateBetween(LocalDate fromEndDate, LocalDate toEndDate);
    List<Rent> findByCreateDateBefore(LocalDate beforeCreateDate);
    List<Rent> findByCreateDateAfter(LocalDate afterCreateDate);
    List<Rent> findByEndDateBefore(LocalDate beforeEndDate);
    List<Rent> findByEndDateAfter(LocalDate afterEndDate);
    List<Rent> findByEquipmentId(Long equipmentId);
    List<Rent> findByResponsibleId(Long responsibleId);
    List<Rent> findByPlacementId(Long placementId);
}

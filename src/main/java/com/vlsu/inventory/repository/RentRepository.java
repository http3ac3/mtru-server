package com.vlsu.inventory.repository;

import com.vlsu.inventory.model.Rent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RentRepository extends JpaRepository<Rent, Long> {
    Page<Rent> findByCreateDate(LocalDate createDate, Pageable pageable);
    Page<Rent> findByEndDate(LocalDate date, Pageable pageable);
    Page<Rent> findByCreateDateBetween(LocalDate fromCreateDate, LocalDate toCreateDate, Pageable pageable);
    Page<Rent> findByEndDateBetween(LocalDate fromEndDate, LocalDate toEndDate, Pageable pageable);
    Page<Rent> findByCreateDateBefore(LocalDate beforeCreateDate, Pageable pageable);
    Page<Rent> findByCreateDateAfter(LocalDate afterCreateDate, Pageable pageable);
    Page<Rent> findByEndDateBefore(LocalDate beforeEndDate, Pageable pageable);
    Page<Rent> findByEndDateAfter(LocalDate afterEndDate, Pageable pageable);
    Page<Rent> findByEquipmentId(Long equipmentId, Pageable pageable);
    Page<Rent> findByResponsibleId(Long responsibleId, Pageable pageable);
    Page<Rent> findByPlacementId(Long placementId, Pageable pageable);
    void deleteByEquipmentId(Long equipmentId);
}

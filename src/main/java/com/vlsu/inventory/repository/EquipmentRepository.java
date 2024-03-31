package com.vlsu.inventory.repository;

import com.vlsu.inventory.model.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    Optional<Equipment> findByInventoryNumber(String inventoryNumber);
    Page<Equipment> findByInventoryNumberContaining(String inventoryNumber, Pageable pageable);
    Page<Equipment> findByName(String name, Pageable pageable);
    Page<Equipment> findByNameContaining(String name, Pageable pageable);
    Page<Equipment> findByCommissioningDate(LocalDate commissioningDate, Pageable pageable);
    Page<Equipment> findByCommissioningActNumber(String commissioningActNumber, Pageable pageable);
    Page<Equipment> findByDecommissioningDate(LocalDate decommissioningDate, Pageable pageable);
    Page<Equipment> findByDecommissioningActNumber(String decommissioningActNumber, Pageable pageable);
    Page<Equipment> findByResponsibleId(Long responsibleId, Pageable pageable);
    Page<Equipment> findByPlacementId(Long placementId, Pageable pageable);
    Page<Equipment> findBySubcategoryId(Long subcategoryId, Pageable pageable);
}

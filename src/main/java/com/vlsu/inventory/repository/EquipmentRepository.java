package com.vlsu.inventory.repository;

import com.vlsu.inventory.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    Optional<Equipment> findByInventoryNumber(String inventoryNumber);
    List<Equipment> findByInventoryNumberContaining(String inventoryNumber);
    List<Equipment> findByName(String name);
    List<Equipment> findByNameContaining(String name);
    List<Equipment> findByCommissioningDate(LocalDate commissioningDate);
    List<Equipment> findByCommissioningActNumber(String commissioningActNumber);
    List<Equipment> findByDecommissioningDate(LocalDate decommissioningDate);
    List<Equipment> findByDecommissioningActNumber(String decommissioningActNumber);
    List<Equipment> findByResponsibleId(Long responsibleId);
    List<Equipment> findByPlacementId(Long placementId);
    List<Equipment> findBySubcategoryId(Long subcategoryId);

}

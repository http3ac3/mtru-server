package com.vlsu.inventory.repository;

import com.vlsu.inventory.model.Rent;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RentRepository extends JpaRepository<Rent, Long>, JpaSpecificationExecutor<Rent> {
    @Override
    @EntityGraph(value = "Rent.equipment.placement.responsible")
    List<Rent> findAll(Specification<Rent> spec);
    @Query("SELECT r FROM Rent r " +
            "JOIN FETCH r.equipment e " +
            "JOIN FETCH r.placement p " +
            "JOIN FETCH r.responsible resp " +
            "WHERE resp.id = ?1 AND r.endDateTime IS NULL")
    List<Rent> findUnclosedByResponsibleId(Long responsibleId);
    @Override
    @EntityGraph(attributePaths = { "equipment", "responsible", "placement" })
    Optional<Rent> findById(Long id);
    List<Rent> findByEquipmentId(Long equipmentId);
    List<Rent> findByResponsibleId(Long responsibleId);
    List<Rent> findByPlacementId(Long placementId);
    void deleteByEquipmentId(Long equipmentId);
    void deleteByResponsibleId(Long responsibleId);
}

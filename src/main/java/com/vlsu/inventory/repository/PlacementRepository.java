package com.vlsu.inventory.repository;

import com.vlsu.inventory.model.Placement;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PlacementRepository extends JpaRepository<Placement, Long> {

    @Query("SELECT p FROM Placement p LEFT JOIN FETCH p.equipment e WHERE p.id = ?1")
    Optional<Placement> findWithEquipmentById(Long id);
}

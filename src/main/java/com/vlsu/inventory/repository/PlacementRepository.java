package com.vlsu.inventory.repository;

import com.vlsu.inventory.model.Placement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlacementRepository extends JpaRepository<Placement, Long> {
    Optional<Placement> findByName(String name);
}

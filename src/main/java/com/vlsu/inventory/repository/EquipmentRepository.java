package com.vlsu.inventory.repository;

import com.vlsu.inventory.model.Equipment;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EquipmentRepository extends JpaRepository<Equipment, Long>, JpaSpecificationExecutor<Equipment> {
    @Override
    @EntityGraph(attributePaths = { "responsible", "placement", "subcategory", "rents", "responsible.user"})
    List<Equipment> findAll(Specification<Equipment> spec);

    @Override
    @EntityGraph(attributePaths = { "responsible", "placement", "subcategory", "rents"})
    List<Equipment> findAll();

    @Override
    @EntityGraph(attributePaths = { "responsible", "placement", "subcategory"})
    Optional<Equipment> findById(Long id);

    @Query("SELECT e FROM Equipment e LEFT JOIN FETCH e.rents r WHERE e.id = ?1")
    Optional<Equipment> findWithRentsById(Long id);

    @Query(value = "UPDATE equipment SET image_data = ?2 WHERE id = ?1", nativeQuery = true)
    boolean updateImageReference(Long id, String imagePath);

    @Query(value = "SELECT image_data FROM equipment WHERE id = ?1", nativeQuery = true)
    String findImageDataById(Long id);
    List<Equipment> findByInventoryNumberStartingWith(String inventoryNumber);
    List<Equipment> findByResponsibleId(Long responsibleId);
    List<Equipment> findByPlacementId(Long placementId);
    List<Equipment> findBySubcategoryId(Long subcategoryId);
}

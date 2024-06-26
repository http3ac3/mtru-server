package com.vlsu.inventory.repository;

import com.vlsu.inventory.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    @EntityGraph(value = "User.responsible.roles")
    List<User> findAll();
    Optional<User> findByUsername(String username);
    Optional<User> findByResponsibleId(Long responsibleId);
    void deleteByResponsibleId(Long responsibleId);
    List<User> findByRolesId(Long roleId);
}

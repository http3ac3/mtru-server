package com.vlsu.inventory.repository;

import com.vlsu.inventory.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByResponsibleId(Long responsibleId);
    List<User> findByRoleId(Long roleId);
}

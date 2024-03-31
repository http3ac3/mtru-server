package com.vlsu.inventory.repository;

import com.vlsu.inventory.model.Role;
import com.vlsu.inventory.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
    List<User> findByUsersId(Long userId);
}
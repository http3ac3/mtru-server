package com.vlsu.inventory.service;

import com.vlsu.inventory.model.Role;
import com.vlsu.inventory.model.User;
import com.vlsu.inventory.repository.RoleRepository;
import com.vlsu.inventory.repository.UserRepository;
import com.vlsu.inventory.util.exception.ResourceNotFoundException;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserById(Long id) throws ResourceNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with id: " + id + " not found"));
    }

    public void addUser(User user) throws ResourceNotFoundException {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(user.getRoles());
        for (Role role : user.getRoles()) {
            role = roleRepository.findByName(role.getName()).orElseThrow(ResourceNotFoundException::new);
            if (role.getUsers() == null) {
                role.setUsers(new ArrayList<>(List.of(user)));
            }
            else
                role.getUsers().add(user);
        }
        userRepository.save(user);
    }

    public void changePasswordByUsername(String username, String password)
            throws ResourceNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with username: " + username + " not found"
                ));
        user.setPassword(password);
        userRepository.save(user);
    }

    public void deleteUserById(Long id) throws ResourceNotFoundException {
        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with id: " + id + " not found"
                ));
        List<Role> userRoles = roleRepository.findByUsersId(id);
        for (Role role : userRoles) {
            System.out.println(role);
            role.getUsers().remove(userToDelete);
        }
        userRepository.deleteById(id);
    }
}

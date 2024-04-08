package com.vlsu.inventory.service;

import com.vlsu.inventory.model.Responsible;
import com.vlsu.inventory.model.Role;
import com.vlsu.inventory.model.User;
import com.vlsu.inventory.repository.ResponsibleRepository;
import com.vlsu.inventory.repository.RoleRepository;
import com.vlsu.inventory.repository.UserRepository;
import com.vlsu.inventory.util.exception.ResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ResponsibleRepository responsibleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, ResponsibleRepository responsibleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.responsibleRepository = responsibleRepository;
    }

    public User getUserById(Long id) throws ResourceNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with id: " + id + " not found"));
    }

    public void addUser(User user, Long responsibleId) throws ResourceNotFoundException {
        Responsible responsible = responsibleRepository.findById(responsibleId)
                        .orElseThrow(() -> new ResourceNotFoundException("Responsible with id '" + responsibleId + "' not found"));
        user.setPassword(user.getPassword());
        user.setResponsible(responsible);
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
            throws UsernameNotFoundException {
        User user = getByUsername(username);
        user.setPassword(password);
        userRepository.save(user);
    }

    public void deleteUserById(Long id) throws ResourceNotFoundException {
        User userToDelete = getUserById(id);
        List<Role> userRoles = roleRepository.findByUsersId(id);
        for (Role role : userRoles) {
            System.out.println(role);
            role.getUsers().remove(userToDelete);
        }
        userRepository.deleteById(id);
    }
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }
    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

}

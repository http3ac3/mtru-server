package com.vlsu.inventory.controller;

import com.vlsu.inventory.model.User;
import com.vlsu.inventory.service.UserService;
import com.vlsu.inventory.util.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/new")
    public ResponseEntity<HttpStatus> createUser(@RequestBody User user, @RequestParam Long responsibleId) throws ResourceNotFoundException {
        userService.addUser(user, responsibleId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUserById(@PathVariable Long id) {
        try {
            userService.deleteUserById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}

package com.vlsu.inventory.controller;

import com.vlsu.inventory.model.User;
import com.vlsu.inventory.service.UserService;
import com.vlsu.inventory.util.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/new")
    public ResponseEntity<HttpStatus> createUser(@RequestBody User user) throws ResourceNotFoundException {
        System.out.println(user.getPassword());
        userService.addUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

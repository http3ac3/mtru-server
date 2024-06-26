package com.vlsu.inventory.controller;

import com.vlsu.inventory.dto.JwtAuthResponse;
import com.vlsu.inventory.dto.RegisterRequest;
import com.vlsu.inventory.dto.SignInRequest;
import com.vlsu.inventory.service.AuthenticationService;
import com.vlsu.inventory.util.exception.ResourceNotFoundException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/auth")
public class AuthController {
    AuthenticationService authenticationService;

    @PostMapping("/sign-in")
    public JwtAuthResponse signIn(@RequestBody SignInRequest request) {
        return authenticationService.signIn(request);
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) throws ResourceNotFoundException {
        try {
            return new ResponseEntity<>(authenticationService.register(request), HttpStatus.CREATED);
        } catch (ResourceNotFoundException exception)  {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

}

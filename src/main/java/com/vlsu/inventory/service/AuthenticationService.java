package com.vlsu.inventory.service;

import com.vlsu.inventory.dto.JwtAuthResponse;
import com.vlsu.inventory.dto.RegisterRequest;
import com.vlsu.inventory.dto.SignInRequest;
import com.vlsu.inventory.model.User;
import com.vlsu.inventory.util.exception.ResourceNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AuthenticationService {
    UserService userService;
    JwtService jwtService;
    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;

    /**
     * Регистрация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthResponse register(RegisterRequest request) throws ResourceNotFoundException {
        User user = new User(request.getUsername(), passwordEncoder.encode(request.getPassword()), request.getRoles());
        userService.addUser(user, request.getResponsible().getId());
        String jwt = jwtService.generateToken(user);
        return new JwtAuthResponse(jwt);
    }

    /**
     * Аутентификация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        UserDetails user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        String jwt = jwtService.generateToken(user);
        return new JwtAuthResponse(jwt);
    }

}

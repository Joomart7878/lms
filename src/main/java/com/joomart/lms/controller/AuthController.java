package com.joomart.lms.controller;

import com.joomart.lms.data_transfer_objects.UserLoginDto;
import com.joomart.lms.data_transfer_objects.UserRegistrationDto;
import com.joomart.lms.model.User;
import com.joomart.lms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles new user registration.
     * Endpoint: POST /api/auth/register
     * @param registrationDto The user data (username and plain-text password).
     * @return 201 Created on success, or 409 Conflict if username is taken.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegistrationDto registrationDto) {
        try {
            User newUser = userService.registerUser(registrationDto);
            return new ResponseEntity<>("User registered successfully with ID: " + newUser.getId(), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // This handles the "Username already taken" scenario from the service layer
            if (e.getMessage().contains("Username already taken")) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>("Registration failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handles basic user login verification.
     * Endpoint: POST /api/auth/login
     * Note: This is a rudimentary check and does not establish a session or return a token (yet).
     * @param loginDto The user credentials (username and plain-text password).
     * @return 200 OK on successful authentication, or 401 Unauthorized otherwise.
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDto loginDto) {
        if (loginDto.getUsername() == null || loginDto.getPassword() == null) {
            return new ResponseEntity<>("Username and password required.", HttpStatus.BAD_REQUEST);
        }

        boolean isAuthenticated = userService.authenticateUser(
                loginDto.getUsername(),
                loginDto.getPassword()
        ).isPresent();

        if (isAuthenticated) {
            return new ResponseEntity<>("Login successful.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid username or password.", HttpStatus.UNAUTHORIZED);
        }
    }

}

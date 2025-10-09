package com.joomart.lms.controller;

import com.joomart.lms.data_transfer_objects.UserLoginDto;
import com.joomart.lms.service.UserService;
import com.joomart.lms.data_transfer_objects.UserRegistrationDto;
import com.joomart.lms.model.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<?> register(
            @Valid @RequestBody UserRegistrationDto registrationDto,
            BindingResult result) {

        // Step 1: Handle validation errors (400 Bad Request)
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        // Step 2: Continue normal registration
        try {
            User newUser = userService.registerUser(registrationDto);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Username already taken")) {
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
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

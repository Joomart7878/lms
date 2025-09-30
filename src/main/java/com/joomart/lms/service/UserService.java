package com.joomart.lms.service;

import com.joomart.lms.data_transfer_objects.UserRegistrationDto;
import com.joomart.lms.model.User;

import java.util.Optional;

public interface UserService {
    /**
     * Registers a new user by hashing the password and persisting the entity.
     * @param userData DTO containing username and plain-text password.
     * @return The newly created User entity.
     * @throws RuntimeException if the username already exists.
     */
    User registerUser(UserRegistrationDto userData);

    /**
     * Finds a user by their unique username.
     * @param username The username to search for.
     * @return An Optional containing the User if found.
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds a user by username and verifies the plain-text password against the hash.
     * @param username The username.
     * @param plainPassword The plain-text password from the login request.
     * @return An Optional containing the User if credentials are valid.
     */
    Optional<User> authenticateUser(String username, String plainPassword);
}

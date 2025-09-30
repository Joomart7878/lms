package com.joomart.lms.service;

import com.joomart.lms.data_transfer_objects.UserRegistrationDto;
import com.joomart.lms.model.User;
import com.joomart.lms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of the UserService interface, handling core user business logic.
 */
@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Dependency Injection via constructor
    public UserServiceImplementation(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user. Hashes the password before saving.
     */
    @Override
    public User registerUser(UserRegistrationDto userData) {
        String username = userData.getUsername().trim();
        String rawPassword = userData.getPassword().trim();

        // Check for existing username (business logic validation)
        if (userRepository.findByUsername(username).isPresent()) {
            // Throw exception to be handled by the controller
            throw new RuntimeException("Username already taken: " + username);
        }

        User newUser = new User();
        newUser.setUsername(username);
        // Hash the CLEANED password for secure storage
        newUser.setPasswordHash(passwordEncoder.encode(rawPassword));

        return userRepository.save(newUser);
    }

    /**
     * Retrieves a user by their username.
     */
    @Override
    public Optional<User> findByUsername(String username) {
        // Find by trimmed username to ensure consistency
        return userRepository.findByUsername(username.trim());
    }

    @Override
    public Optional<User> authenticateUser(String username, String plainPassword) {
        return Optional.empty();
    }

    /**
     * Authenticates a user based on username and plain-text password.
     * @return The authenticated User object if successful, otherwise throws an exception.
     */
    public User loginUser(String username, String rawPassword) {

        // 1. Find the user by CLEANED username
        String cleanedUsername = username.trim();

        // Use the repository directly here to simplify the lookup flow
        User user = userRepository.findByUsername(cleanedUsername)
                .orElseThrow(() -> new RuntimeException("Invalid username or password."));

        // 2. Compare the CLEANED raw password with the stored hash
        // We trim the raw password before matching against the hash saved during registration.
        boolean passwordMatches = passwordEncoder.matches(rawPassword.trim(), user.getPasswordHash());

        if (passwordMatches) {
            return user; // Success
        } else {
            // Throw the same generic message for security, preventing username enumeration
            throw new RuntimeException("Invalid username or password.");
        }
    }
}

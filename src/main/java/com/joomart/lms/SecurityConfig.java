package com.joomart.lms;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


// Configuration class for security-related beans, specifically the PasswordEncoder.
@Configuration
public class SecurityConfig {
    /**
     * Declares the BCryptPasswordEncoder bean. BCrypt is a strong, widely accepted
     * hashing algorithm necessary for securely storing user passwords.
     * @return The configured PasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

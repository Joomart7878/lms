package com.joomart.lms;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Configuration class for security-related beans, specifically the PasswordEncoder
 * and initial configuration to exempt public endpoints from requiring authentication.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Declares the BCryptPasswordEncoder bean.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the security filter chain to allow public access to authentication endpoints.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Disable CSRF for simplicity in a stateless API (required for POST requests to work easily)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Configure endpoint authorization rules
                .authorizeHttpRequests(authorize -> authorize
                        // Allow all requests to the H2 console (useful for development/debugging)
                        .requestMatchers("/h2-console/**").permitAll()
                        // Allow all requests to the registration and login endpoints
                        .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                        // Require authentication for all other requests
                        .anyRequest().authenticated()
                )

                // 3. Enable HTTP Basic or Form login for testing any authenticated endpoint
                .httpBasic(withDefaults());

        // 4. Important for H2: allow frames since H2 console runs in a frame
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }
}

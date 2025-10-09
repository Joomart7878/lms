package com.joomart.lms;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.joomart.lms.model.User;
import com.joomart.lms.data_transfer_objects.UserRegistrationDto;
import com.joomart.lms.repository.UserRepository;
import com.joomart.lms.exception.UsernameAlreadyExistsException; // adapt if different

import com.joomart.lms.service.UserServiceImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceImplementationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImplementation userServiceImplementation;

    @Test
    void registerUser_success() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("alicetest");
        dto.setEmail("alice@exampletest.com");
        dto.setPassword("secret123")

        when(userRepository.findByUsername("alicetest")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("secret123")).thenReturn("encodedPwd");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });

        User result = userServiceImplementation.registerUser(dto);

        assertNotNull(result);
        assertEquals("alicetest", result.getUsername());
        verify(passwordEncoder).encode("secret123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_usernameExists_throws() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("bob"); // must match what registerUser reads
        dto.setPassword("pw");
        dto.setEmail("bob@example.com");

        // Mock the correct repository method
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(new User()));

        assertThrows(UsernameAlreadyExistsException.class,
                () -> userServiceImplementation.registerUser(dto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void findByUsername_found() {
        User u = new User();
        u.setUsername("sam");
        when(userRepository.findByUsername("sam")).thenReturn(Optional.of(u));

        Optional<User> result = userServiceImplementation.findByUsername("sam");
        assertTrue(result.isPresent());
        assertEquals("sam", result.get().getUsername());
    }

    @Test
    void findByUsername_notFound_throws() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        Optional<User> result = userServiceImplementation.findByUsername("unknown");
        assertTrue(result.isEmpty(), "Expected Optional to be empty when username not found");
    }
}

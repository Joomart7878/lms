package com.joomart.lms;

import com.joomart.lms.controller.AuthController;
import com.joomart.lms.data_transfer_objects.UserRegistrationDto;
import com.joomart.lms.model.User;
import com.joomart.lms.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @Test
    void postRegister_success() throws Exception {
        // Create local MockMvc instance for this test
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(userService)).build();

        // JSON payload for registration
        String payload = "{\"username\":\"jane\",\"email\":\"jane@example.com\",\"password\":\"secret\"}";

        // Mock the service response
        User saved = new User();
        saved.setId(10L);
        saved.setUsername("jane");
        saved.setEmail("jane@example.com");

        when(userService.registerUser(any(UserRegistrationDto.class))).thenReturn(saved);

        // Perform POST request and verify response
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("jane"))
                .andExpect(jsonPath("$.email").value("jane@example.com"));
    }

    @Test
    void postRegister_invalidInput_badRequest() throws Exception {

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(userService)).build();

        String payload = "{\"username\":\"\",\"email\":\"not-an-email\",\"password\":\"pw\"}";

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }
}
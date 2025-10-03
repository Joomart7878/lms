package com.joomart.lms.data_transfer_objects;

/**
 * Data Transfer Object for carrying user registration request data (username and password).
 */

public class UserRegistrationDto {
    private String username;
    private String password;
    private String email;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) { this.email = email; }
}

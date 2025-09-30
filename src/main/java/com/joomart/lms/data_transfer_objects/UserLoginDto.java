package com.joomart.lms.data_transfer_objects;

/**
 * Data Transfer Object for carrying user login request data (username and password).
 */

public class UserLoginDto {
    private String username;
    private String password;

    // Standard Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

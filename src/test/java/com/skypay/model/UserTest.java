package com.skypay.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    
    @Test
    void testCreateUser() {
        // Given
        int userId = 1;
        int balance = 5000;
        
        // When
        User user = new User(userId, balance);
        
        // Then
        assertEquals(userId, user.getUserId());
        assertEquals(balance, user.getBalance());
    }
    
    @Test
    void testUpdateUser() {
        // Given
        User user = new User(1, 5000);
        
        // When
        user.setUserId(2);
        user.setBalance(10000);
        
        // Then
        assertEquals(2, user.getUserId());
        assertEquals(10000, user.getBalance());
    }
    
    @Test
    void testToString() {
        // Given
        User user = new User(1, 5000);
        
        // When
        String userString = user.toString();
        
        // Then
        assertTrue(userString.contains("userId=1"));
        assertTrue(userString.contains("balance=5000"));
    }
}

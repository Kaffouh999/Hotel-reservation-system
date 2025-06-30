package com.skypay.model;

import java.util.Date;

public class User {
    private int userId;
    private int balance;
    private final Date createdAt;
    
    public User(int userId, int balance) {
        this.userId = userId;
        this.balance = balance;
        this.createdAt = new Date();
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getBalance() {
        return balance;
    }
    
    public void setBalance(int balance) {
        this.balance = balance;
    }
    
    public Date getCreatedAt() {
        return new Date(createdAt.getTime());
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", balance=" + balance +
                ", createdAt=" + createdAt +
                '}';
    }
}
package com.skypay;

import com.skypay.enums.RoomType;
import com.skypay.service.Service;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        Service service = new Service();
        
        service.setRoom(1, RoomType.STANDARD, 1000);
        service.setRoom(2, RoomType.JUNIOR_SUITE, 2000);  
        service.setRoom(3, RoomType.MASTER_SUITE, 3000);
        
        service.setUser(1, 5000);
        service.setUser(2, 10000);
        
        try {
            service.bookRoom(1, 2, new Date(2026-1900, 5, 30), new Date(2026-1900, 6, 7));
        } catch (Exception e) {
            System.out.println("Booking failed: " + e.getMessage());
        }
        
        try {
            service.bookRoom(1, 2, new Date(2026-1900, 6, 7), new Date(2026-1900, 5, 30));
        } catch (Exception e) {
            System.out.println("Booking failed: " + e.getMessage());
        }
        
        try {
            service.bookRoom(1, 1, new Date(2026-1900, 6, 7), new Date(2026-1900, 6, 8));
        } catch (Exception e) {
            System.out.println("Booking failed: " + e.getMessage());
        }
        
        try {
            service.bookRoom(2, 1, new Date(2026-1900, 6, 7), new Date(2026-1900, 6, 9));
        } catch (Exception e) {
            System.out.println("Booking failed: " + e.getMessage());
        }
        
        try {
            service.bookRoom(2, 3, new Date(2026-1900, 6, 7), new Date(2026-1900, 6, 8));
        } catch (Exception e) {
            System.out.println("Booking failed: " + e.getMessage());
        }
        
        service.setRoom(1, RoomType.MASTER_SUITE, 10000);
        
        System.out.println("=== PRINT ALL ===");
        service.printAll();
        
        System.out.println("\n=== PRINT ALL USERS ===");
        service.printAllUsers();
    }
}
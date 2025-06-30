package com.skypay.service;

import com.skypay.enums.RoomType;
import com.skypay.model.Room;
import com.skypay.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {
    
    private Service service;
    
    @BeforeEach
    void setUp() {
        service = new Service();
    }
    
    @Test
    void testAddNewRoom() throws Exception {
        // Given
        int roomNumber = 101;
        RoomType roomType = RoomType.STANDARD;
        int price = 1000;
        
        // When
        service.setRoom(roomNumber, roomType, price);
        
        // Then
        Field roomsField = Service.class.getDeclaredField("rooms");
        roomsField.setAccessible(true);
        ArrayList<Room> rooms = (ArrayList<Room>) roomsField.get(service);
        
        assertEquals(1, rooms.size());
        assertEquals(roomNumber, rooms.get(0).getRoomNumber());
        assertEquals(roomType, rooms.get(0).getRoomType());
        assertEquals(price, rooms.get(0).getPricePerNight());
    }
    
    @Test
    void testUpdateExistingRoom() throws Exception {
        // Given
        int roomNumber = 101;
        service.setRoom(roomNumber, RoomType.STANDARD, 1000);
        
        // When
        RoomType newRoomType = RoomType.MASTER_SUITE;
        int newPrice = 3000;
        service.setRoom(roomNumber, newRoomType, newPrice);
        
        // Then
        Field roomsField = Service.class.getDeclaredField("rooms");
        roomsField.setAccessible(true);
        ArrayList<Room> rooms = (ArrayList<Room>) roomsField.get(service);
        
        assertEquals(1, rooms.size());
        assertEquals(roomNumber, rooms.get(0).getRoomNumber());
        assertEquals(newRoomType, rooms.get(0).getRoomType());
        assertEquals(newPrice, rooms.get(0).getPricePerNight());
    }
    
    @Test
    void testSetRoomWithNegativePrice() {
        // Given
        int roomNumber = 101;
        RoomType roomType = RoomType.STANDARD;
        int price = -1000;
        
        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.setRoom(roomNumber, roomType, price);
        });
        
        assertEquals("Room price cannot be negative", exception.getMessage());
    }
    
    @Test
    void testAddNewUser() throws Exception {
        // Given
        int userId = 1;
        int balance = 5000;
        
        // When
        service.setUser(userId, balance);
        
        // Then
        Field usersField = Service.class.getDeclaredField("users");
        usersField.setAccessible(true);
        ArrayList<User> users = (ArrayList<User>) usersField.get(service);
        
        assertEquals(1, users.size());
        assertEquals(userId, users.get(0).getUserId());
        assertEquals(balance, users.get(0).getBalance());
    }
    
    @Test
    void testUpdateExistingUser() throws Exception {
        // Given
        int userId = 1;
        service.setUser(userId, 5000);
        
        // When
        int newBalance = 10000;
        service.setUser(userId, newBalance);
        
        // Then
        Field usersField = Service.class.getDeclaredField("users");
        usersField.setAccessible(true);
        ArrayList<User> users = (ArrayList<User>) usersField.get(service);
        
        assertEquals(1, users.size());
        assertEquals(userId, users.get(0).getUserId());
        assertEquals(newBalance, users.get(0).getBalance());
    }
    
    @Test
    void testSetUserWithNegativeBalance() {
        // Given
        int userId = 1;
        int balance = -5000;
        
        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.setUser(userId, balance);
        });
        
        assertEquals("Balance cannot be negative", exception.getMessage());
    }
    
    @Test
    void testBookRoomSuccessfully() {
        // Given
        int userId = 1;
        int roomNumber = 101;
        int initialBalance = 5000;
        int pricePerNight = 1000;
        Date checkIn = createDate(2026, Calendar.JULY, 1);
        Date checkOut = createDate(2026, Calendar.JULY, 3);  // 2 nights
        
        service.setUser(userId, initialBalance);
        service.setRoom(roomNumber, RoomType.STANDARD, pricePerNight);
        
        // When
        service.bookRoom(userId, roomNumber, checkIn, checkOut);
        
        // Then - Verify user's balance was deducted correctly
        Field usersField;
        try {
            usersField = Service.class.getDeclaredField("users");
            usersField.setAccessible(true);
            ArrayList<User> users = (ArrayList<User>) usersField.get(service);
            assertEquals(initialBalance - (2 * pricePerNight), users.get(0).getBalance());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    void testBookRoomWithNonExistentUser() {
        // Given
        int nonExistentUserId = 999;
        int roomNumber = 101;
        Date checkIn = createDate(2026, Calendar.JULY, 1);
        Date checkOut = createDate(2026, Calendar.JULY, 3);
        
        service.setRoom(roomNumber, RoomType.STANDARD, 1000);
        
        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.bookRoom(nonExistentUserId, roomNumber, checkIn, checkOut);
        });
        
        assertTrue(exception.getMessage().contains("User with ID 999 not found"));
    }
    
    @Test
    void testBookRoomWithNonExistentRoom() {
        // Given
        int userId = 1;
        int nonExistentRoomNumber = 999;
        Date checkIn = createDate(2026, Calendar.JULY, 1);
        Date checkOut = createDate(2026, Calendar.JULY, 3);
        
        service.setUser(userId, 5000);
        
        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.bookRoom(userId, nonExistentRoomNumber, checkIn, checkOut);
        });
        
        assertTrue(exception.getMessage().contains("Room with number 999 not found"));
    }
    
    @Test
    void testBookRoomWithInvalidDateRange() {
        // Given
        int userId = 1;
        int roomNumber = 101;
        Date checkIn = createDate(2026, Calendar.JULY, 3);
        Date checkOut = createDate(2026, Calendar.JULY, 1);  // Before check-in
        
        service.setUser(userId, 5000);
        service.setRoom(roomNumber, RoomType.STANDARD, 1000);
        
        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.bookRoom(userId, roomNumber, checkIn, checkOut);
        });
        
        assertEquals("Check-out date must be after check-in date", exception.getMessage());
    }
    
    @Test
    void testBookRoomWithSameDates() {
        // Given
        int userId = 1;
        int roomNumber = 101;
        Date sameDate = createDate(2026, Calendar.JULY, 1);
        
        service.setUser(userId, 5000);
        service.setRoom(roomNumber, RoomType.STANDARD, 1000);
        
        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.bookRoom(userId, roomNumber, sameDate, sameDate);
        });
        
        assertEquals("Check-out date must be after check-in date", exception.getMessage());
    }
    
    @Test
    void testBookRoomWithInsufficientBalance() {
        // Given
        int userId = 1;
        int roomNumber = 101;
        int insufficientBalance = 500;
        int pricePerNight = 1000;
        Date checkIn = createDate(2026, Calendar.JULY, 1);
        Date checkOut = createDate(2026, Calendar.JULY, 3);  // 2 nights = 2000 cost
        
        service.setUser(userId, insufficientBalance);
        service.setRoom(roomNumber, RoomType.STANDARD, pricePerNight);
        
        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.bookRoom(userId, roomNumber, checkIn, checkOut);
        });
        
        assertTrue(exception.getMessage().startsWith("Insufficient balance"));
    }
    
    @Test
    void testBookRoomWithNullDates() {
        // Given
        int userId = 1;
        int roomNumber = 101;
        
        service.setUser(userId, 5000);
        service.setRoom(roomNumber, RoomType.STANDARD, 1000);
        
        // When & Then - Null check-in
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
            service.bookRoom(userId, roomNumber, null, createDate(2026, Calendar.JULY, 3));
        });
        
        assertEquals("Check-in and check-out dates cannot be null", exception1.getMessage());
        
        // When & Then - Null check-out
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            service.bookRoom(userId, roomNumber, createDate(2026, Calendar.JULY, 1), null);
        });
        
        assertEquals("Check-in and check-out dates cannot be null", exception2.getMessage());
    }
    
    @Test
    void testBookRoomWhenAlreadyBooked() {
        // Given
        int userId1 = 1;
        int userId2 = 2;
        int roomNumber = 101;
        int pricePerNight = 1000;
        
        // First booking: July 1-3
        Date checkIn1 = createDate(2026, Calendar.JULY, 1);
        Date checkOut1 = createDate(2026, Calendar.JULY, 3);
        
        // Second booking with overlap: July 2-4
        Date checkIn2 = createDate(2026, Calendar.JULY, 2);
        Date checkOut2 = createDate(2026, Calendar.JULY, 4);
        
        service.setUser(userId1, 5000);
        service.setUser(userId2, 5000);
        service.setRoom(roomNumber, RoomType.STANDARD, pricePerNight);
        
        // Book the room for the first time
        service.bookRoom(userId1, roomNumber, checkIn1, checkOut1);
        
        // When & Then - Try to book again for overlapping dates
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.bookRoom(userId2, roomNumber, checkIn2, checkOut2);
        });
        
        assertTrue(exception.getMessage().contains("is not available for the selected dates"));
    }
    
    @Test
    void testBookRoomNonOverlappingDates() {
        // Given
        int userId1 = 1;
        int userId2 = 2;
        int roomNumber = 101;
        int pricePerNight = 1000;
        
        // First booking: July 1-3
        Date checkIn1 = createDate(2026, Calendar.JULY, 1);
        Date checkOut1 = createDate(2026, Calendar.JULY, 3);
        
        // Second booking with no overlap: July 4-6
        Date checkIn2 = createDate(2026, Calendar.JULY, 4);
        Date checkOut2 = createDate(2026, Calendar.JULY, 6);
        
        service.setUser(userId1, 5000);
        service.setUser(userId2, 5000);
        service.setRoom(roomNumber, RoomType.STANDARD, pricePerNight);
        
        // When
        service.bookRoom(userId1, roomNumber, checkIn1, checkOut1);
        service.bookRoom(userId2, roomNumber, checkIn2, checkOut2);
        
        // Then - Both bookings should succeed
        // Verify by checking bookings exist
        try {
            Field bookingsField = Service.class.getDeclaredField("bookings");
            bookingsField.setAccessible(true);
            ArrayList<?> bookings = (ArrayList<?>) bookingsField.get(service);
            assertEquals(2, bookings.size());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    // Helper method to create a date more easily for testing
    private Date createDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}

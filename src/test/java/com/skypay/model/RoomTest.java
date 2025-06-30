package com.skypay.model;

import com.skypay.enums.RoomType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {
    
    @Test
    void testCreateRoom() {
        // Given
        int roomNumber = 101;
        RoomType roomType = RoomType.STANDARD;
        int pricePerNight = 1000;
        
        // When
        Room room = new Room(roomNumber, roomType, pricePerNight);
        
        // Then
        assertEquals(roomNumber, room.getRoomNumber());
        assertEquals(roomType, room.getRoomType());
        assertEquals(pricePerNight, room.getPricePerNight());
    }
    
    @Test
    void testUpdateRoom() {
        // Given
        Room room = new Room(101, RoomType.STANDARD, 1000);
        
        // When
        room.setRoomNumber(102);
        room.setRoomType(RoomType.JUNIOR_SUITE);
        room.setPricePerNight(2000);
        
        // Then
        assertEquals(102, room.getRoomNumber());
        assertEquals(RoomType.JUNIOR_SUITE, room.getRoomType());
        assertEquals(2000, room.getPricePerNight());
    }
    
    @Test
    void testToString() {
        // Given
        Room room = new Room(101, RoomType.STANDARD, 1000);
        
        // When
        String roomString = room.toString();
        
        // Then
        assertTrue(roomString.contains("roomNumber=101"));
        assertTrue(roomString.contains("roomType=STANDARD"));
        assertTrue(roomString.contains("pricePerNight=1000"));
    }
}

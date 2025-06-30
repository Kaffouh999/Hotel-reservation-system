package com.skypay.model;

import com.skypay.enums.RoomType;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {
    
    @Test
    void testCreateBooking() {
        // Given
        int userId = 1;
        int roomNumber = 101;
        Date checkIn = createDate(2026, Calendar.JULY, 1);
        Date checkOut = createDate(2026, Calendar.JULY, 3);
        int totalCost = 2000;
        RoomType roomType = RoomType.STANDARD;
        int roomPrice = 1000;
        int userBalanceAtBooking = 5000;
        
        // When
        Booking booking = new Booking(
            userId, roomNumber, checkIn, checkOut, totalCost,
            roomType, roomPrice, userBalanceAtBooking
        );
        
        // Then
        assertEquals(userId, booking.getUserId());
        assertEquals(roomNumber, booking.getRoomNumber());
        assertEquals(checkIn, booking.getCheckIn());
        assertEquals(checkOut, booking.getCheckOut());
        assertEquals(totalCost, booking.getTotalCost());
        assertEquals(roomType, booking.getRoomType());
        assertEquals(roomPrice, booking.getRoomPrice());
        assertEquals(userBalanceAtBooking, booking.getUserBalanceAtBooking());
        assertNotNull(booking.getBookingTimestamp());
        assertTrue(booking.getBookingId() > 0);
    }
    
    @Test
    void testBookingIdsAreUnique() {
        // Given
        Date checkIn = createDate(2026, Calendar.JULY, 1);
        Date checkOut = createDate(2026, Calendar.JULY, 3);
        
        // When
        Booking booking1 = new Booking(
            1, 101, checkIn, checkOut, 2000,
            RoomType.STANDARD, 1000, 5000
        );
        
        Booking booking2 = new Booking(
            1, 102, checkIn, checkOut, 4000,
            RoomType.MASTER_SUITE, 2000, 10000
        );
        
        // Then
        assertEquals(booking1.getBookingId() + 1, booking2.getBookingId());
    }
    
    @Test
    void testBookingReturnsCopiesOfDates() {
        // Given
        Date checkIn = createDate(2026, Calendar.JULY, 1);
        Date checkOut = createDate(2026, Calendar.JULY, 3);
        
        // When
        Booking booking = new Booking(
            1, 101, checkIn, checkOut, 2000,
            RoomType.STANDARD, 1000, 5000
        );
        
        Date returnedCheckIn = booking.getCheckIn();
        Date returnedCheckOut = booking.getCheckOut();
        
        // Then - modify returned dates and verify original dates in booking are unchanged
        returnedCheckIn.setTime(0);
        returnedCheckOut.setTime(0);
        
        assertNotEquals(0, booking.getCheckIn().getTime());
        assertNotEquals(0, booking.getCheckOut().getTime());
    }
    
    @Test
    void testToString() {
        // Given
        Date checkIn = createDate(2026, Calendar.JULY, 1);
        Date checkOut = createDate(2026, Calendar.JULY, 3);
        
        Booking booking = new Booking(
            1, 101, checkIn, checkOut, 2000,
            RoomType.STANDARD, 1000, 5000
        );
        
        // When
        String bookingString = booking.toString();
        
        // Then
        assertTrue(bookingString.contains("bookingId="));
        assertTrue(bookingString.contains("userId=1"));
        assertTrue(bookingString.contains("roomNumber=101"));
        assertTrue(bookingString.contains("totalCost=2000"));
        assertTrue(bookingString.contains("roomType=STANDARD"));
        assertTrue(bookingString.contains("roomPrice=1000"));
        assertTrue(bookingString.contains("userBalanceAtBooking=5000"));
    }
    
    // Helper method to create a date more easily for testing
    private Date createDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}

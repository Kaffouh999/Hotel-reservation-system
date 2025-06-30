package com.skypay.model;

import com.skypay.enums.RoomType;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class Booking {
    private static final AtomicInteger counter = new AtomicInteger(0);
    
    private final int bookingId;
    private final int userId;
    private final int roomNumber;
    private final Date checkIn;
    private final Date checkOut;
    private final int totalCost;
    private final Date bookingTimestamp;
    
    // Snapshot data
    private final RoomType roomType;
    private final int roomPrice;
    private final int userBalanceAtBooking;
    
    public Booking(int userId, int roomNumber, Date checkIn, Date checkOut, 
                   int totalCost, RoomType roomType, int roomPrice, int userBalanceAtBooking) {
        this.bookingId = counter.incrementAndGet();
        this.userId = userId;
        this.roomNumber = roomNumber;
        this.checkIn = new Date(checkIn.getTime());
        this.checkOut = new Date(checkOut.getTime());
        this.totalCost = totalCost;
        this.bookingTimestamp = new Date();
        
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.userBalanceAtBooking = userBalanceAtBooking;
    }
    
    // Getters only as this is an immutable object
    public int getBookingId() {
        return bookingId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public int getRoomNumber() {
        return roomNumber;
    }
    
    public Date getCheckIn() {
        return new Date(checkIn.getTime());
    }
    
    public Date getCheckOut() {
        return new Date(checkOut.getTime());
    }
    
    public int getTotalCost() {
        return totalCost;
    }
    
    public Date getBookingTimestamp() {
        return new Date(bookingTimestamp.getTime());
    }
    
    public RoomType getRoomType() {
        return roomType;
    }
    
    public int getRoomPrice() {
        return roomPrice;
    }
    
    public int getUserBalanceAtBooking() {
        return userBalanceAtBooking;
    }
    
    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", userId=" + userId +
                ", roomNumber=" + roomNumber +
                ", checkIn=" + checkIn +
                ", checkOut=" + checkOut +
                ", totalCost=" + totalCost +
                ", bookingTimestamp=" + bookingTimestamp +
                ", roomType=" + roomType +
                ", roomPrice=" + roomPrice +
                ", userBalanceAtBooking=" + userBalanceAtBooking +
                '}';
    }
}
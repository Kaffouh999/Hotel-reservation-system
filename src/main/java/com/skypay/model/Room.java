package com.skypay.model;

import com.skypay.enums.RoomType;
import java.util.Date;

public class Room {
    private int roomNumber;
    private RoomType roomType;
    private int pricePerNight;
    private final Date createdAt;
    
    public Room(int roomNumber, RoomType roomType, int pricePerNight) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.createdAt = new Date();
    }
    
    public int getRoomNumber() {
        return roomNumber;
    }
    
    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
    
    public RoomType getRoomType() {
        return roomType;
    }
    
    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }
    
    public int getPricePerNight() {
        return pricePerNight;
    }
    
    public void setPricePerNight(int pricePerNight) {
        this.pricePerNight = pricePerNight;
    }
    
    public Date getCreatedAt() {
        return new Date(createdAt.getTime());
    }
    
    @Override
    public String toString() {
        return "Room{" +
                "roomNumber=" + roomNumber +
                ", roomType=" + roomType +
                ", pricePerNight=" + pricePerNight +
                ", createdAt=" + createdAt +
                '}';
    }
}
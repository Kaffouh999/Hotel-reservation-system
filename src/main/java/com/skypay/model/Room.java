package com.skypay.model;

import com.skypay.enums.RoomType;

public class Room {
    private int roomNumber;
    private RoomType roomType;
    private int pricePerNight;
    
    public Room(int roomNumber, RoomType roomType, int pricePerNight) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
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
    
    @Override
    public String toString() {
        return "Room{" +
                "roomNumber=" + roomNumber +
                ", roomType=" + roomType +
                ", pricePerNight=" + pricePerNight +
                '}';
    }
}
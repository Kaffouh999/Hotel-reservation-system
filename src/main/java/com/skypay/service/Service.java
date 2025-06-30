package com.skypay.service;

import com.skypay.enums.RoomType;
import com.skypay.model.Booking;
import com.skypay.model.Room;
import com.skypay.model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Service {
    ArrayList<Room> rooms;
    ArrayList<User> users;
    ArrayList<Booking> bookings;
    
    public Service() {
        this.rooms = new ArrayList<>();
        this.users = new ArrayList<>();
        this.bookings = new ArrayList<>();
    }
    
    public void setRoom(int roomNumber, RoomType roomType, int roomPricePerNight) {
        if (roomPricePerNight < 0) {
            throw new IllegalArgumentException("Room price cannot be negative");
        }
        
        Optional<Room> existingRoom = rooms.stream()
                .filter(r -> r.getRoomNumber() == roomNumber)
                .findFirst();
        
        if (existingRoom.isPresent()) {
            Room room = existingRoom.get();
            room.setRoomType(roomType);
            room.setPricePerNight(roomPricePerNight);
        } else {
            rooms.add(new Room(roomNumber, roomType, roomPricePerNight));
        }
    }
    
    public void setUser(int userId, int balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        
        Optional<User> existingUser = users.stream()
                .filter(u -> u.getUserId() == userId)
                .findFirst();
        
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setBalance(balance);
        } else {
            users.add(new User(userId, balance));
        }
    }
    
    public void bookRoom(int userId, int roomNumber, Date checkIn, Date checkOut) {
        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("Check-in and check-out dates cannot be null");
        }
        
        Date normalizedCheckIn = normalizeDate(checkIn);
        Date normalizedCheckOut = normalizeDate(checkOut);
        
        if (normalizedCheckOut.before(normalizedCheckIn) || normalizedCheckOut.equals(normalizedCheckIn)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }
        
        User user = users.stream()
                .filter(u -> u.getUserId() == userId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found"));
        
        Room room = rooms.stream()
                .filter(r -> r.getRoomNumber() == roomNumber)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Room with number " + roomNumber + " not found"));
        
        boolean isRoomAvailable = checkRoomAvailability(roomNumber, normalizedCheckIn, normalizedCheckOut);
        if (!isRoomAvailable) {
            throw new IllegalArgumentException("Room " + roomNumber + " is not available for the selected dates");
        }
        
        int nights = calculateNights(normalizedCheckIn, normalizedCheckOut);
        int totalCost = nights * room.getPricePerNight();
        
        if (user.getBalance() < totalCost) {
            throw new IllegalArgumentException("Insufficient balance. Required: " + totalCost + ", Available: " + user.getBalance());
        }
        
        Booking booking = new Booking(
                userId,
                roomNumber,
                normalizedCheckIn,
                normalizedCheckOut,
                totalCost,
                room.getRoomType(),
                room.getPricePerNight(),
                user.getBalance()
        );
        bookings.add(booking);
        
        user.setBalance(user.getBalance() - totalCost);
        
        System.out.println("Booking successful: " + booking.getBookingId());
    }
    
    public void printAll() {
        System.out.println("=== ROOMS ===");
        rooms.stream()
                .sorted(Comparator.comparing(Room::getRoomNumber))
                .forEach(System.out::println);
        
        System.out.println("\n=== BOOKINGS ===");
        bookings.stream()
                .sorted(Comparator.comparing(Booking::getBookingTimestamp).reversed())
                .forEach(booking -> {
                    System.out.println("Booking ID: " + booking.getBookingId() +
                            ", User ID: " + booking.getUserId() +
                            ", Room: " + booking.getRoomNumber() +
                            ", Room Type (at booking): " + booking.getRoomType() +
                            ", Price (at booking): " + booking.getRoomPrice() +
                            ", Check-in: " + booking.getCheckIn() +
                            ", Check-out: " + booking.getCheckOut() +
                            ", Total Cost: " + booking.getTotalCost() +
                            ", User Balance (at booking): " + booking.getUserBalanceAtBooking() +
                            ", Booking Time: " + booking.getBookingTimestamp());
                });
    }
    
    public void printAllUsers() {
        System.out.println("=== USERS ===");
        users.stream()
                .sorted(Comparator.comparing(User::getUserId))
                .forEach(user -> {
                    System.out.println("User ID: " + user.getUserId() + ", Balance: " + user.getBalance());
                });
    }
    
    private boolean checkRoomAvailability(int roomNumber, Date checkIn, Date checkOut) {
        List<Booking> roomBookings = bookings.stream()
                .filter(b -> b.getRoomNumber() == roomNumber)
                .collect(Collectors.toList());
        
        for (Booking booking : roomBookings) {
            Date existingCheckIn = normalizeDate(booking.getCheckIn());
            Date existingCheckOut = normalizeDate(booking.getCheckOut());
            
            if (!(checkOut.before(existingCheckIn) || checkIn.after(existingCheckOut))) {
                return false;
            }
        }
        
        return true;
    }
    
    private int calculateNights(Date checkIn, Date checkOut) {
        long diffInMillies = checkOut.getTime() - checkIn.getTime();
        return (int) (diffInMillies / (1000 * 60 * 60 * 24));
    }
    
    private Date normalizeDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
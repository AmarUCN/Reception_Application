package Model;

import java.time.LocalDate;

public class Booking {

    private Guest guest;
    private Room room;
    private LocalDate reservationStart;
    private LocalDate reservationEnd;
    private int bookingID;

    
    public Booking(Guest guest, Room room, LocalDate reservationStart, LocalDate reservationEnd) {
        this.guest = guest;
        this.room = room;
        this.reservationStart = reservationStart;
        this.reservationEnd = reservationEnd;
    }
    
    public int getBookingID() {
    	return bookingID;
    }
    
    public void setBookingID(int bookingID) {
    	this.bookingID = bookingID;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDate getReservationStart() {
        return reservationStart;
    }

    public void setReservationStart(LocalDate reservationStart) {
        this.reservationStart = reservationStart;
    }

    public LocalDate getReservationEnd() {
        return reservationEnd;
    }

    public void setReservationEnd(LocalDate reservationEnd) {
        this.reservationEnd = reservationEnd;
    }
    
    public void addRoom(String roomClass, int roomNumber, String roomPhone) {
        Room newRoom = new Room(roomClass,  roomPhone);
        setRoom(newRoom); 
    }

}

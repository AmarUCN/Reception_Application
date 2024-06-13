package Controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import DAO.IBookingDAO;
import DB_Layer.BookingDB;
import DB_Layer.DataAccessException;
import Model.Booking;
import Model.Guest;
import Model.Room;

public class BookingController {
    
    private IBookingDAO bookingDB;
    private Booking booking;
    private RoomController roomController;
    private GuestController guestController;
    
    public BookingController() throws DataAccessException {
        bookingDB = new BookingDB();
        guestController = new GuestController();
        roomController = new RoomController();
    }
    
    public List<Room> checkReservations(LocalDate date) throws SQLException, DataAccessException {
        return roomController.checkRooms(date);
    }
    
    public void removeLock(int roomNumber, LocalDate reservationStart) throws SQLException, DataAccessException {
        roomController.removeReservation(reservationStart, roomNumber);
    }
    
    public Booking finalizeBooking() throws DataAccessException, SQLException {
        Booking doneBooking = null;
        if (booking != null) {
            int guestID = guestController.addGuestDB(booking.getGuest());
            // Remove the temporary lock with pseudo order/customer
            roomController.removeReservation(booking.getReservationStart(), booking.getRoom().getRoomNumber());
            // Add booking to DB, generating a bookingId
            doneBooking = bookingDB.addBooking(booking, guestID);
            // Create a new reservation, locking the table with date
            roomController.reserveRoom(doneBooking.getBookingID(), doneBooking.getReservationStart(), doneBooking.getRoom(), doneBooking.getReservationEnd());
            this.booking = null;
        }
        return doneBooking;
    }
    
    // Create a new booking, store locally in bookingCtrl for handling
    public Booking createBooking(Guest guest, LocalDate reservationStart, Room room, LocalDate reservationEnd) throws DataAccessException, SQLException {
        if (booking == null) {
            // Create temporary lock on table+date with pseudo-order+pseudo-customer
            roomController.reserveRoom(1, reservationStart, room, reservationEnd); // 1 as a placeholder bookingId
            booking = new Booking(guest, room, reservationStart, reservationEnd);
        }
        return booking;
    }
    
    public Booking getBooking() {
    	return booking;
    }
    
    public RoomController getRoomController() {
    	return roomController;
    }
    
    public GuestController getGuestController() {
    	return guestController;
    }
    
    
    
}

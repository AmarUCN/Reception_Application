package DAO;

import java.sql.SQLException;

import DB_Layer.DataAccessException;
import Model.Booking;

public interface IBookingDAO {
	
	Booking addBooking(Booking booking, int guestID)throws DataAccessException, SQLException;

}

package Controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import DAO.IRoomDAO;
import DAO.IReservationDAO;
import DB_Layer.RoomDB;
import Model.Room;
import DB_Layer.DataAccessException;
import DB_Layer.ReservationDB;

public class RoomController {
	
	private IRoomDAO roomDB;
	private IReservationDAO reservationDB;

	public RoomController()throws DataAccessException {
		reservationDB = new ReservationDB();
		roomDB = new RoomDB();
		
	}
	
	public List<Room> checkRooms(LocalDate date) throws DataAccessException, SQLException {
        return roomDB.checkRooms(date);
    }
	
	public void reserveRoom(int bookingId, LocalDate reservationStart,Room room, LocalDate reservationEnd) throws DataAccessException, SQLException {
		reservationDB.reserveRoom(bookingId, reservationStart,room,reservationEnd);
	}
	
	public void removeReservation(LocalDate reservationStart, int roomNumber) throws SQLException, DataAccessException {
		reservationDB.deleteRoomReservation(reservationStart, roomNumber);
	}
	

}

package DAO;

import java.sql.SQLException;
import java.time.LocalDate;


import DB_Layer.DataAccessException;
import Model.Room;

public interface IReservationDAO {
	void deleteRoomReservation(LocalDate reservationDate, int roomNumber) throws SQLException, DataAccessException;

	void reserveRoom(int bookingId, LocalDate reservationStart, Room room, LocalDate reservationEnd)
			throws DataAccessException, SQLException;

	int getDBCount() throws SQLException;
}

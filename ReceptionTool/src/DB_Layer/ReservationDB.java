package DB_Layer;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;


import DAO.IReservationDAO;
import Model.Room;


public class ReservationDB implements IReservationDAO{

	private static final String reserveRoomQ = "INSERT INTO reservation VALUES(?,?,?,?)"; 
	private static final String deleteRoomReservationQ = "DELETE FROM reservation "
			+ "WHERE roomNumber = ? AND reservationStart = ? AND reservationEnd = ?";
	private PreparedStatement reserveRoom, deleteRoomReservation;
	private DBConnection dbConnection;

	public ReservationDB() throws DataAccessException {
		dbConnection = DBConnection.getInstance();
		try {
			reserveRoom = dbConnection.getConnection().prepareStatement(reserveRoomQ);
			deleteRoomReservation = dbConnection.getConnection().prepareStatement(deleteRoomReservationQ);
		} catch (Exception e) {
			throw new DataAccessException(e, "Could not prepare statement");
		}
	}

	@Override
	public void reserveRoom(int bookingId, LocalDate reservationStart, Room room, LocalDate reservationEnd) throws DataAccessException, SQLException
			{

		try {
			dbConnection.startTransaction();
			reserveRoom.setInt(1, bookingId);
			reserveRoom.setInt(2, room.getRoomNumber());
			reserveRoom.setDate(3, Date.valueOf(reservationStart));
			reserveRoom.setDate(4, Date.valueOf(reservationEnd));
			reserveRoom.executeUpdate();
			dbConnection.commitTransaction();
		} catch (SQLException e) {
			try {
				dbConnection.rollbackTransaction();
			} catch (DataAccessException e1) {
				throw e1;
			}
		}
	}


	@Override
	public void deleteRoomReservation(LocalDate reservationStart, int roomNumber)
			throws SQLException, DataAccessException {
		try {
			dbConnection.startTransaction();
			deleteRoomReservation.setInt(1, roomNumber);
			deleteRoomReservation.setDate(2, Date.valueOf(reservationStart));
			deleteRoomReservation.execute();
			dbConnection.commitTransaction();
		} catch (SQLException e) {
			dbConnection.rollbackTransaction();
			throw new DataAccessException(e, "Delete room reservation failed");
		}

	}

	@Override
	public int getDBCount() throws SQLException {
		return checkDBCount();
	}

	//returns the count of inserts in reservationDB.
	private int checkDBCount() throws SQLException {
		String query = "SELECT COUNT(*) FROM reservation";
		PreparedStatement dbCount = dbConnection.getConnection().prepareStatement(query);
		ResultSet rs = dbCount.executeQuery();
		rs.next();
		return rs.getInt(1);

	}

	
}

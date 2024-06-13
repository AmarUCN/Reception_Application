package DAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import DB_Layer.DataAccessException;
import Model.Room;

public interface IRoomDAO {
	
	List<Room> checkRooms(LocalDate date )throws DataAccessException, SQLException;

}



package DB_Layer;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import DAO.IRoomDAO;
import Model.Room;

public class RoomDB implements IRoomDAO {

    private static final String checkRoomsQ = "SELECT roomNumber, roomClass, roomPhone FROM [room]\r\n"
            + "WHERE roomNumber NOT IN (\r\n"
            + "    SELECT roomNumber FROM reservation\r\n"
            + "    WHERE reservationStart <= ? AND reservationEnd >= ?\r\n"
            + ")";
    private PreparedStatement checkRooms;
    private DBConnection dbconnection;

    public RoomDB() throws DataAccessException {
        try {
            dbconnection = DBConnection.getInstance();
            checkRooms = dbconnection.getConnection().prepareStatement(checkRoomsQ);
        } catch (Exception e) {
            throw new DataAccessException(e, "Could not prepare statement");
        }
    }

    @Override
    public List<Room> checkRooms(LocalDate date) throws DataAccessException, SQLException {
        checkRooms.setDate(1, Date.valueOf(date));
        checkRooms.setDate(2, Date.valueOf(date));
        ResultSet rs = checkRooms.executeQuery();
        List<Room> rooms = new LinkedList<>();
        while (rs.next()) {
            Room room = new Room(rs.getString("roomClass"), rs.getString("roomPhone"));
            room.setRoomNumber(rs.getInt("roomNumber"));
            rooms.add(room);
        }
        return rooms;
    }
}

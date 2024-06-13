package Controller;

import java.sql.SQLException;
import java.util.List;

import DAO.IGuestDAO;
import DB_Layer.DataAccessException;
import DB_Layer.GuestDB;
import Model.Guest;

public class GuestController {

    private IGuestDAO guestDB;
    
    public GuestController() throws DataAccessException {
        guestDB = new GuestDB();
    }
    
    public int addGuestDB(Guest guest) throws SQLException, DataAccessException {
        return guestDB.addGuest(guest);
    }
    
    public Guest createGuest(String firstName, String lastName) {
        return new Guest(firstName, lastName);
    }

    public Guest findGuestByID(int guestID) throws SQLException, DataAccessException {
        return guestDB.findGuest(guestID);
    }
    
    
}

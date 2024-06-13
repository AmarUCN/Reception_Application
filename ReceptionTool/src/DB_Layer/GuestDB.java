package DB_Layer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import DAO.IGuestDAO;
import Model.Guest;

public class GuestDB implements IGuestDAO {

    private static final String addGuestQ = "INSERT INTO guest (firstName, lastName) VALUES (?, ?)";
    private static final String findGuestByIDQ = "SELECT * FROM guest WHERE guestID = ?";
    private static final String updateGuestQ = "UPDATE guest SET firstName = ?, lastName = ? WHERE guestID = ?";
    private static final String deleteGuestQ = "DELETE FROM guest WHERE guestID = ?";
    private static final String getAllGuestsQ = "SELECT * FROM guest";
    private PreparedStatement findGuestByID, addGuest, updateGuest, deleteGuest, getAllGuests;
    private DBConnection dbconnection;

    public GuestDB() throws DataAccessException {
        try {
            dbconnection = DBConnection.getInstance();
            findGuestByID = dbconnection.getConnection().prepareStatement(findGuestByIDQ);
            addGuest = dbconnection.getConnection().prepareStatement(addGuestQ, Statement.RETURN_GENERATED_KEYS);
            updateGuest = dbconnection.getConnection().prepareStatement(updateGuestQ);
            deleteGuest = dbconnection.getConnection().prepareStatement(deleteGuestQ);
            getAllGuests = dbconnection.getConnection().prepareStatement(getAllGuestsQ);
        } catch (Exception e) {
            throw new DataAccessException(e, "Could not prepare statement");
        }
    }

    private Guest buildGuestObject(ResultSet rs) throws SQLException {
        Guest guest = new Guest(rs.getString("firstName"), rs.getString("lastName"));
        guest.setGuestID(rs.getInt("guestID"));
        return guest;
    }

    @Override
    public int addGuest(Guest guest) throws DataAccessException, SQLException {
        try {
            dbconnection.startTransaction();
            addGuest.setString(1, guest.getFirstName());
            addGuest.setString(2, guest.getLastName());
            int guestID = dbconnection.executeInsertWithIdentity(addGuest);
            dbconnection.commitTransaction();
            return guestID;
        } catch (DataAccessException | SQLException e) {
            dbconnection.rollbackTransaction();
            throw new DataAccessException(e, "Could not add guest to database");
        }
    }

    @Override
    public Guest findGuest(int guestID) throws DataAccessException, SQLException {
        Guest guest = null;
        try {
            findGuestByID.setInt(1, guestID);
            ResultSet rs = findGuestByID.executeQuery();
            if (rs.next()) {
                guest = buildGuestObject(rs);
            }
        } catch (SQLException e) {
            throw e;
        }
        return guest;
    }

    
}

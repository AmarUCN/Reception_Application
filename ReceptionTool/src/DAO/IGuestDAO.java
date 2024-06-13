package DAO;

import java.sql.SQLException;
import java.util.List;

import DB_Layer.DataAccessException;
import Model.Guest;

public interface IGuestDAO {
    int addGuest(Guest guest) throws DataAccessException, SQLException;
    Guest findGuest(int guestID) throws DataAccessException, SQLException;
}

package DB_Layer;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import DAO.IBookingDAO;
import Model.Booking;

public class BookingDB implements IBookingDAO {
    private static final String addBookingQ = "INSERT INTO booking (guest_id, room_number, reservation_start, reservation_end) VALUES (?, ?, ?, ?)";
    private PreparedStatement addBookingStatement;
    private DBConnection dbConnection;

    public BookingDB() throws DataAccessException {
        try {
            dbConnection = DBConnection.getInstance();
            Connection connection = dbConnection.getConnection();
            addBookingStatement = connection.prepareStatement(addBookingQ, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            throw new DataAccessException(e, "Could not prepare statement");
        }
    }

    @Override
    public Booking addBooking(Booking booking, int guestID) throws DataAccessException, SQLException {
        try {
            dbConnection.startTransaction();

            addBookingStatement.setInt(1, guestID);
            addBookingStatement.setInt(2, booking.getRoom().getRoomNumber());
            addBookingStatement.setDate(3, Date.valueOf(booking.getReservationStart()));
            addBookingStatement.setDate(4, Date.valueOf(booking.getReservationEnd()));

            int affectedRows = addBookingStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating booking failed, no rows affected.");
            }

            try (ResultSet generatedKeys = addBookingStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    booking.setBookingID(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating booking failed, no ID obtained.");
                }
            }

            dbConnection.commitTransaction();

        } catch (SQLException e) {
            dbConnection.rollbackTransaction();
            throw new DataAccessException(e, "Could not add booking to database");
        }

        return booking;
    }
}

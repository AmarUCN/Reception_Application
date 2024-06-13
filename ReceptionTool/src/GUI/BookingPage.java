package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.UtilDateModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import java.util.Date;
import java.util.Calendar;

import Controller.BookingController;
import DB_Layer.DataAccessException;
import Model.Guest;
import Model.Room;

public class BookingPage extends JPanel {

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JDatePickerImpl startDatePicker;
    private JDatePickerImpl endDatePicker;
    private JComboBox<Room> roomComboBox;
    private BookingController bookingController;

    public BookingPage() {
        setLayout(new BorderLayout());

        // Initialize booking controller
        try {
            bookingController = new BookingController();
        } catch (DataAccessException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error initializing controllers: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Guest details panel
        JPanel guestPanel = new JPanel(new GridLayout(2, 2));
        guestPanel.add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        guestPanel.add(firstNameField);
        guestPanel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        guestPanel.add(lastNameField);

        // Date pickers panel
        JPanel datePanel = new JPanel(new GridLayout(2, 2));
        datePanel.add(new JLabel("Reservation Start Date:"));
        startDatePicker = createDatePicker();
        datePanel.add(startDatePicker);
        datePanel.add(new JLabel("Reservation End Date:"));
        endDatePicker = createDatePicker();
        datePanel.add(endDatePicker);

        // Room selection combo box
        roomComboBox = new JComboBox<>();
        roomComboBox.setRenderer(new RoomListCellRenderer());
        JPanel roomPanel = new JPanel(new BorderLayout());
        roomPanel.add(new JLabel("Select Room:"), BorderLayout.WEST);
        roomPanel.add(roomComboBox, BorderLayout.CENTER);

        // Check rooms button
        JButton checkRoomsButton = new JButton("Check Rooms");
        checkRoomsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    checkRooms();
                } catch (SQLException | DataAccessException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(BookingPage.this, "Error checking rooms: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Finalize booking button
        JButton finalizeBookingButton = new JButton("Finalize Booking");
        finalizeBookingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    finalizeBooking();
                } catch (SQLException | DataAccessException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(BookingPage.this, "Error finalizing booking: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(checkRoomsButton);
        buttonPanel.add(finalizeBookingButton);

        // Add components to main panel
        add(guestPanel, BorderLayout.NORTH);
        add(datePanel, BorderLayout.CENTER);
        add(roomPanel, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JDatePickerImpl createDatePicker() {
        UtilDateModel model = new UtilDateModel();
        Properties properties = new Properties();
        properties.put("text.today", "Today");
        properties.put("text.month", "Month");
        properties.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, properties);
        return new JDatePickerImpl(datePanel, new DateLabelFormatter());
    }

    private void checkRooms() throws SQLException, DataAccessException {
        Date selectedDate = (Date) startDatePicker.getModel().getValue();
        if (selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Please select a start date.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate date = convertToLocalDate(selectedDate);
        List<Room> rooms = bookingController.checkReservations(date);
        roomComboBox.removeAllItems(); // Clear existing items

        for (Room room : rooms) {
            roomComboBox.addItem(room);
        }
    }

    private void finalizeBooking() throws SQLException, DataAccessException {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter guest's first and last name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Date startDate = (Date) startDatePicker.getModel().getValue();
        Date endDate = (Date) endDatePicker.getModel().getValue();

        if (startDate == null || endDate == null) {
            JOptionPane.showMessageDialog(this, "Please select both start and end dates.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate reservationStart = convertToLocalDate(startDate);
        LocalDate reservationEnd = convertToLocalDate(endDate);

        Room selectedRoom = (Room) roomComboBox.getSelectedItem();
        if (selectedRoom == null) {
            JOptionPane.showMessageDialog(this, "Please select a room from the list.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Guest guest = new Guest(firstName, lastName);
        bookingController.createBooking(guest, reservationStart, selectedRoom, reservationEnd);
        bookingController.finalizeBooking();

        JOptionPane.showMessageDialog(this, "Booking finalized successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private LocalDate convertToLocalDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    public void updateRoomList() {
        try {
            checkRooms();
        } catch (SQLException | DataAccessException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating room list: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Custom renderer for Room objects in the combo box
    private class RoomListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Room) {
                Room room = (Room) value;
                setText("Room " + room.getRoomNumber() + " (" + room.getRoomClass() + ")");
            }
            return this;
        }
    }
}

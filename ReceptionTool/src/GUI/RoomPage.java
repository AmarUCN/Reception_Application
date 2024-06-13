package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import Controller.BookingController;
import Controller.RoomController;
import DB_Layer.DataAccessException;
import Model.Room;

public class RoomPage extends JPanel {

    private JTable roomTable;
    private DefaultTableModel tableModel;
    private BookingController bookingController;
    private RoomController roomController;
    private JTextField dateField;
    private JButton checkRoomsButton;

    public RoomPage() {
        setLayout(new BorderLayout());

        try {
            bookingController = new BookingController();
            roomController = new RoomController();
        } catch (DataAccessException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error initializing controllers: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Table setup
        String[] columnNames = {"Room Number", "Room Class", "Room Phone"};
        tableModel = new DefaultTableModel(columnNames, 0);
        roomTable = new JTable(tableModel);
        add(new JScrollPane(roomTable), BorderLayout.CENTER);

        // Panel for controls
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        // Date field
        controlPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        dateField = new JTextField(10);
        controlPanel.add(dateField);

        // Check rooms button
        checkRoomsButton = new JButton("Check Rooms");
        checkRoomsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    checkRooms();
                } catch (SQLException | DataAccessException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(RoomPage.this, "Error checking rooms: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        controlPanel.add(checkRoomsButton);

        add(controlPanel, BorderLayout.NORTH);
    }

    private void checkRooms() throws SQLException, DataAccessException {
        String dateText = dateField.getText();
        LocalDate date;

        try {
            date = LocalDate.parse(dateText);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please enter a date in the format YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Room> rooms = roomController.checkRooms(date);
        tableModel.setRowCount(0); // Clear existing rows

        for (Room room : rooms) {
            Object[] rowData = {room.getRoomNumber(), room.getRoomClass(), room.getRoomPhone()};
            tableModel.addRow(rowData);
        }
    }
}
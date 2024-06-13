package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;


import Controller.GuestController;
import DB_Layer.DataAccessException;
import Model.Guest;

public class GuestPage extends JPanel {

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTable guestTable;
    private DefaultTableModel tableModel;
    private GuestController guestController;

    public GuestPage() {
        setLayout(new BorderLayout());

        // Initialize guest controller
        try {
            guestController = new GuestController();
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

        // Table setup
        String[] columnNames = {"Guest ID", "First Name", "Last Name"};
        tableModel = new DefaultTableModel(columnNames, 0);
        guestTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(guestTable);

        // Buttons
        JButton addButton = new JButton("Add Guest");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addGuest();
                } catch (SQLException | DataAccessException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(GuestPage.this, "Error adding guest: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        

        

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        

        // Add components to main panel
        add(guestPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        
    }

    

    private void addGuest() throws SQLException, DataAccessException {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter guest's first and last name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Guest guest = guestController.createGuest(firstName, lastName);
        guestController.addGuestDB(guest);
        
        JOptionPane.showMessageDialog(this, "Guest added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

   

    
}

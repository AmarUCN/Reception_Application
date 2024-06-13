package GUI;

import Controller.ThreadPollServer;
import javax.swing.*;
import java.awt.*;
import DB_Layer.DataAccessException;

public class Main {

    public static void main(String[] args) {
        // Set up the main frame
        JFrame mainFrame = new JFrame("Hotel Management System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600);
        mainFrame.setLayout(new BorderLayout());

        // Initialize the pages
        BookingPage bookingPage = new BookingPage();
        GuestPage guestPage = new GuestPage();
        RoomPage roomPage = new RoomPage();

        // Tabbed pane to hold different pages
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Booking", bookingPage);
        tabbedPane.addTab("Guests", guestPage);
        tabbedPane.addTab("Rooms", roomPage);

        // Add the tabbed pane to the main frame
        mainFrame.add(tabbedPane, BorderLayout.CENTER);

        // Start the ThreadPollServer
        try {
            ThreadPollServer threadPollServer = new ThreadPollServer();
            threadPollServer.start();
        } catch (DataAccessException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainFrame, "Error starting database poller: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Start the UpdateRoom thread
        UpdateRoom updateRoomThread = new UpdateRoom(bookingPage);
        updateRoomThread.start();

        // Display the main frame
        mainFrame.setVisible(true);
    }
}

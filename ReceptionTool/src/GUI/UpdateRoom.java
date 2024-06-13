package GUI;

import Controller.RoomMonitor;

public class UpdateRoom extends Thread {

    private RoomMonitor monitor;
    private BookingPage bookingPage;

    public UpdateRoom(BookingPage bookingPage) {
        this.monitor = RoomMonitor.getInstance();
        this.bookingPage = bookingPage;
    }

    @Override
    public void run() {
        while (true) {
            monitor.waitMethod();
            bookingPage.updateRoomList(); 
        }
    }
}

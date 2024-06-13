package Controller;

import java.sql.SQLException;

import DB_Layer.DataAccessException;
import DB_Layer.ReservationDB;
import DAO.IReservationDAO;



public class ThreadPollServer extends Thread{
	private int currNum;
	private IReservationDAO reservationDB;
	
	public ThreadPollServer() throws DataAccessException {
		reservationDB = new ReservationDB();
	}

	@Override
	public void run() {
		while (true) {
			try {
				pollDB();
				Thread.sleep(1500);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void pollDB() throws SQLException {
		int newNum = reservationDB.getDBCount();
		if (currNum != newNum) {
			RoomMonitor monitor = RoomMonitor.getInstance();
			monitor.notifyAllThreads();
			currNum = newNum;
		}
	}
}

package Controller;

public class RoomMonitor {
	private static RoomMonitor instance;

	private RoomMonitor() {
		
	}
	
	public static RoomMonitor getInstance() {
		if (instance == null) {
			instance = new RoomMonitor();
		}
		return instance;
	}

	public synchronized void notifyAllThreads() {
		notifyAll();
	}
	
	
	public synchronized void waitMethod() {
		try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
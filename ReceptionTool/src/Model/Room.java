package Model;

public class Room {
	
	private int roomNumber;
	private String roomClass;
	private String roomPhone;
	

	public Room(String roomClass, String roomPhone) {
		this.roomClass = roomClass;
		this.roomPhone = roomPhone;
		
	}

	public int getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}

	public String getRoomClass() {
		return roomClass;
	}

	public void setRoomClass(String roomClass) {
		this.roomClass = roomClass;
	}
	
	public String getRoomPhone() {
		return roomPhone;
	}
	
	public void setRoomPhone(String roomPhone) {
		this.roomPhone = roomPhone;
	}
	
	
}



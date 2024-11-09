package application;

public class Room {
	private int roomID;
	private String description;
	private boolean isBooked;
	public Room() {
		roomID = 0;
		description = "";
		isBooked = false;
	}
	public int getRoomID() {
		return roomID;
	}
	public void setRoomID(int roomID) {
		this.roomID = roomID;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isBooked() {
		return isBooked;
	}
	public void setBooked(boolean isBooked) {
		this.isBooked = isBooked;
	}

}

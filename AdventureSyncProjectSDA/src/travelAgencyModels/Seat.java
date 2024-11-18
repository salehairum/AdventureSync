package travelAgencyModels;

public class Seat {
	private int seatID;
	private int busID;
	boolean isBooked;
	int rowNo;

	public Seat(int seatID, int busID, boolean isBooked, int rowNo) {
        this.seatID = seatID;
        this.busID = busID;
        this.isBooked = isBooked;
        this.rowNo = rowNo;
    }
	//default booked=false
	public Seat(int seatID, int busID, int rowNo) {
        this.seatID = seatID;
        this.busID = busID;
        this.isBooked = false;
        this.rowNo = rowNo;
    }
	
	//getters and setters
	public int getSeatID() {
		return seatID;
	}
	public void setSeatID(int seatID) {
		this.seatID = seatID;
	}
	public int getBusID() {
		return busID;
	}
	public void setBusID(int busID) {
		this.busID = busID;
	}
	public boolean isBooked() {
		return isBooked;
	}
	public void setBooked(boolean isBooked) {
		this.isBooked = isBooked;
	}
	public int getRowNo() {
		return rowNo;
	}
	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}
}
